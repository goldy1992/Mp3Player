package com.github.goldy1992.mp3player.client.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.ui.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.WindowSize
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.client.viewmodels.FolderScreenViewModel
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils.isEmpty

@Composable
fun FolderScreen(
    navController: NavController,
    windowSize : WindowSize = WindowSize.Compact,
    viewModel: FolderScreenViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()

    val folderItems by viewModel.folderChildren.collectAsState()

    val isLargeScreen = windowSize == WindowSize.Expanded
    if (isLargeScreen) {
        LargeFolderScreen(
            folderName = viewModel.folderName,
            navController = navController,
            mediaController = viewModel.mediaController,
            currentMediaItemState = viewModel.currentMediaItem,
            isPlayingState = viewModel.isPlaying,
            scope = scope,
            folderItems = folderItems
        )
    } else {
        SmallFolderScreen(
            folderName = viewModel.folderName,
            navController = navController,
            mediaController = viewModel.mediaController,
            currentMediaItemState = viewModel.currentMediaItem,
            isPlayingState = viewModel.isPlaying,
            scope = scope,
            folderItems = folderItems
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SmallFolderScreen(
    folderName : String = Constants.UNKNOWN,
    folderPath : String = Constants.UNKNOWN,
    navController: NavController,
    mediaController : MediaControllerAdapter,
    isPlayingState: StateFlow<Boolean>,
    currentMediaItemState : StateFlow<MediaItem>,
    scope : CoroutineScope = rememberCoroutineScope(),
    folderItems : List<MediaItem>
) {
    val drawerState : DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerContent = {
            NavigationDrawerContent(
                navController = navController
            ) },
        drawerState = drawerState) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Column {
                            Text(
                                text = folderName,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
//                            Text(
//                                text = folderPath,
//                                style = MaterialTheme.typography.subtitle2,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis
//                            )
                        }
                    },

                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                navController.popBackStack()
                            }
                        }) {
                            Icon(Icons.Filled.ArrowBack, "Back")
                        }
                    },
                    actions = {},
                )
            },
            bottomBar = {
                PlayToolbar(mediaController = mediaController,
                            isPlayingState = isPlayingState) {
                    navController.navigate(Screen.NOW_PLAYING.name)
                }
            },

            content = {
                if (isEmpty(folderItems)) {
                    Surface(
                        Modifier
                            .fillMaxSize()
                            .padding(it)) {
                        Column(
                            Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                } else {
                    SongList(
                        songs = folderItems!!,
                        isPlayingState = isPlayingState,
                        currentMediaItemState = currentMediaItemState) {
                        itemIndex, mediaItemList ->
                        val mediaItem = mediaItemList[itemIndex]
                        Log.i("ON_CLICK_SONG", "clicked song with id : ${mediaItem.mediaId}")
                        mediaController.playFromSongList(itemIndex, mediaItemList)

                    }
                }
            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@Composable
private fun LargeFolderScreen(
    folderName : String = Constants.UNKNOWN,
    navController: NavController,
    mediaController : MediaControllerAdapter,
    currentMediaItemState: StateFlow<MediaItem>,
    isPlayingState: StateFlow<Boolean>,
    scope : CoroutineScope = rememberCoroutineScope(),
    folderItems : List<MediaItem>?
) {

    PermanentNavigationDrawer(
        drawerContent = {
            NavigationDrawerContent(
                navController = navController,
                currentScreen = Screen.FOLDER
            ) },
    ) {
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = {
                        Column {
                            Text(
                                text = folderName,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
//                            Text(
//                                text = folderPath,
//                                style = MaterialTheme.typography.subtitle2,
//                                maxLines = 1,
//                                overflow = TextOverflow.Ellipsis
//                            )
                        }
                    },

                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                navController.popBackStack()
                            }
                        }) {
                            Icon(Icons.Filled.ArrowBack, "Back",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    },
                    actions = {},
                )
            },
            bottomBar = {
                PlayToolbar(mediaController = mediaController, isPlayingState = isPlayingState) {
                    navController.navigate(Screen.NOW_PLAYING.name)
                }
            },

            content = {
                Surface(
                    Modifier
                        .width(500.dp)
                        .padding(it)) {
                    if (isEmpty(folderItems)) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        SongList(songs = folderItems!!, currentMediaItemState = currentMediaItemState, isPlayingState = isPlayingState) {
                            itemIndex, mediaItemList ->
                            val mediaItem = mediaItemList[itemIndex]
                            Log.i("ON_CLICK_SONG", "clicked song with id : ${mediaItem.mediaId}")
                            mediaController.playFromSongList(itemIndex, mediaItemList)

                        }
                    }
            }
        })
    }

}