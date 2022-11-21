package com.github.goldy1992.mp3player.client.ui.screens

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.ui.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.ui.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.WindowSize
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.client.viewmodels.FolderScreenViewModel
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils.isEmpty

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun FolderScreen(
    navController: NavController = rememberAnimatedNavController(),
    windowSize : WindowSize = WindowSize.Compact,
    viewModel: FolderScreenViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val mediaController = viewModel.mediaController
    val folderItems by viewModel.folderChildren.collectAsState()
    val currentMediaItem by viewModel.currentMediaItem.collectAsState()
    val folderName = viewModel.folderName

    val onSongSelected : (Int, List<MediaItem>) -> Unit = { itemIndex, mediaItemList ->
        val mediaItem = mediaItemList[itemIndex]
        Log.i("ON_CLICK_SONG", "clicked song with id : ${mediaItem.mediaId}")
        mediaController.playFromSongList(itemIndex, mediaItemList)
    }

    val bottomBar : @Composable () -> Unit = {
        PlayToolbar(isPlayingProvider= { isPlaying },
            mediaController = viewModel.mediaController,
            navController = navController,
            scope = scope)
    }

    val navDrawerContent : @Composable () -> Unit = {
        NavigationDrawerContent(
            navController = navController,
            currentScreen = Screen.FOLDER
        )
    }

    val screenContent : @Composable (PaddingValues) -> Unit = {
        FolderScreenContent(
            modifier = Modifier.padding(it),
            isPlayingProvider = {isPlaying},
            folderItemsProvider = { folderItems},
            currentMediaItemProvider = { currentMediaItem },
            onSongSelected = onSongSelected
        )
    }

    val isLargeScreen = windowSize == WindowSize.Expanded
    if (isLargeScreen) {
        LargeFolderScreen(
            topBar = {
                TopAppBar(
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
            bottomBar = bottomBar,
            navDrawerContent = navDrawerContent,
            content = screenContent
        )
    } else {
        SmallFolderScreen(
            topBar = {
                TopAppBar(
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
            navDrawerContent = navDrawerContent,
            bottomBar = bottomBar,
            content = screenContent
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SmallFolderScreen(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    navDrawerContent: @Composable () -> Unit = {},
    drawerState : DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    content: @Composable (PaddingValues) -> Unit = {}
) {
    ModalNavigationDrawer(
        drawerContent = navDrawerContent,
        drawerState = drawerState) {
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar,
            content = content
        )
    }

}


@OptIn(ExperimentalCoilApi::class)
@Composable
private fun FolderScreenContent(modifier : Modifier = Modifier,
    folderItemsProvider : () -> List<MediaItem> = { emptyList() },
    isPlayingProvider : () -> Boolean = { false},
    currentMediaItemProvider : () -> MediaItem = {MediaItem.EMPTY},
    onSongSelected : (Int, List<MediaItem>) -> Unit = {_,_ ->}) {
    Column(modifier = modifier) {
        val folderItems = folderItemsProvider()
        if (isEmpty(folderItems)) {
            Surface(
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
            ) {
                CircularProgressIndicator()
            }
        }
        else {
            SongList(
                songs = folderItems,
                isPlayingProvider = isPlayingProvider,
                currentMediaItemProvider = currentMediaItemProvider,
                onSongSelected = onSongSelected
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LargeFolderScreen(
    navDrawerContent : @Composable () -> Unit,
    topBar : @Composable () -> Unit,
    bottomBar : @Composable () -> Unit,
    content : @Composable (PaddingValues) -> Unit
) {

    PermanentNavigationDrawer(
        drawerContent = navDrawerContent,
    ) {
        Scaffold(
            topBar = topBar,
            bottomBar = bottomBar,
            content = content)
    }

}