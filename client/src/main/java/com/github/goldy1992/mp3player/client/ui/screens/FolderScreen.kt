package com.github.goldy1992.mp3player.client.ui.screens

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
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.data.Folder
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.data.Songs
import com.github.goldy1992.mp3player.client.ui.WindowSize
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.ui.lists.songs.SongList
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class)
@Composable
fun FolderScreen(
    navController: NavController = rememberAnimatedNavController(),
    windowSize : WindowSize = WindowSize.Compact,
    viewModel: FolderScreenViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentSong by viewModel.currentMediaItem.collectAsState()
    val folder : Folder by viewModel.folder.collectAsState()

    val onSongSelected : (Int, Songs) -> Unit = { itemIndex, mediaItemList ->
        viewModel.playFromSongList(itemIndex, mediaItemList)
    }

    val bottomBar : @Composable () -> Unit = {
        PlayToolbar(
            isPlayingProvider = { isPlaying },
            onClickPlay = { viewModel.play() },
            onClickPause = {viewModel.pause() },
            onClickSkipPrevious = { viewModel.skipToPrevious() },
            onClickSkipNext = { viewModel.skipToNext() },
            onClickBar = { navController.navigate(Screen.NOW_PLAYING.name)}
        )
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
            folderSongs = { folder.songs },
            currentSong = { currentSong },
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
                                text = folder.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            /* TODO: Add a column giving the folder information,
                            i.e.
                            The folder path,
                            Whether the folder display is recursive,
                            The total duration of all songs in the folder.
                            */

//                            Text(
//                                text = folder.path,
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
                                text = folder.name,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
//                            Text(
//                                text = folder.path,
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
                                folderSongs : () -> Songs = { Songs.NOT_LOADED },
                                isPlayingProvider : () -> Boolean = { false},
                                currentSong : () -> Song = {Song()},
                                onSongSelected : (Int, Songs) -> Unit = {_,_ ->}) {
    Column(modifier = modifier) {
        val folderItems = folderSongs()
        when (folderItems.state) {
            State.LOADING -> {
                Surface(
                    modifier = modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    CircularProgressIndicator()
                }
            }
            State.LOADED -> {
                SongList(
                    songs = folderItems,
                    isPlayingProvider = isPlayingProvider,
                    currentSongProvider = currentSong,
                    onSongSelected = onSongSelected
                )
            }
            else -> {
            }
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