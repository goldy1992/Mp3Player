package com.github.goldy1992.mp3player.client.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.OpenInFull
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.models.media.PlaybackState
import com.github.goldy1992.mp3player.client.models.media.Folder
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.ui.UiConstants.DEFAULT_DP_SIZE
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.navigation.NavigationDrawerContent
import com.github.goldy1992.mp3player.client.ui.lists.songs.EmptySongsList
import com.github.goldy1992.mp3player.client.ui.lists.songs.LoadedSongsListWithHeader
import com.github.goldy1992.mp3player.client.utils.TimeUtils
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val LOG_TAG = "FolderScreen"


@Composable
fun SharedTransitionScope.FolderScreen(
    navController: NavController = rememberNavController(),
    windowSize : WindowSizeClass = WindowSizeClass.calculateFromSize(DEFAULT_DP_SIZE),
    viewModel: FolderScreenViewModel = viewModel(),
    animatedContentScope: AnimatedContentScope

) {
    val scope = rememberCoroutineScope()
    val playbackState by viewModel.playbackState.collectAsState()
    val folder : Folder by viewModel.folder.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val onSongSelected : (Int, Playlist) -> Unit = { itemIndex, _ -> viewModel.playPlaylist(folder.playlist, itemIndex) }

    val bottomBar : @Composable () -> Unit = {
        PlayToolbar(
            animatedVisibilityScope = animatedContentScope,
            playbackState = playbackState,
            onClickBar = { navController.navigate(Screen.NOW_PLAYING.name)},
        )
    }

    val topBar : @Composable () -> Unit = {
        LargeTopAppBar(
            scrollBehavior = scrollBehavior,
            title = {
                Column {
                    Text(
                        modifier = Modifier.basicMarquee(),
                        text = folder.name,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        navController.popBackStack()
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            },
            actions = {},
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
            playbackStateProvider = {playbackState},
            folderProvider = { folder },
            onSongSelected = onSongSelected
        )
    }

    val isLargeScreen = windowSize.widthSizeClass == WindowWidthSizeClass.Expanded
    if (isLargeScreen) {
        LargeFolderScreen(
            topBar = topBar,
            bottomBar = bottomBar,
            navDrawerContent = navDrawerContent,
            content = screenContent
        )
    } else {
        SmallFolderScreen(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = topBar,
            navDrawerContent = navDrawerContent,
            bottomBar = bottomBar,
            content = screenContent
        )

    }
}

@Composable
private fun SmallFolderScreen(
    modifier : Modifier = Modifier,
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
            modifier = modifier,
            topBar = topBar,
            bottomBar = bottomBar,
            content = content
        )
    }

}


@OptIn(ExperimentalCoilApi::class)
@Composable
private fun FolderScreenContent(modifier : Modifier = Modifier,
                                folderProvider : () -> Folder = { Folder() },
                                playbackStateProvider : () -> PlaybackState = { PlaybackState.DEFAULT },
                                onSongSelected : (Int, Playlist) -> Unit = { _, _->}) {
    Column(modifier = modifier) {
        val folder = folderProvider()
        val playbackState = playbackStateProvider()
        val folderSongs = folder.playlist
        when (folderSongs.state) {
            State.LOADING -> {
                Surface(
                    modifier = modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    CircularProgressIndicator()
                }
            }
            State.LOADED -> {
                Log.d(LOG_TAG, "FolderScreenContent() folder songs state LOADED, size: ${folderSongs.songs.size}")
                LoadedSongsListWithHeader(
                    playlist = folderSongs,
                    isPlayingProvider = playbackState.isPlaying,
                    currentSong = playbackState.currentSong,
                    onSongSelected = onSongSelected,
                    headerItem = { HeaderItem(folder = folder)}
                )
            }
            else -> {
                EmptySongsList()
            }
        }
    }
}

@Preview
@Composable
private fun HeaderItem(
    modifier: Modifier = Modifier,
    folder: Folder = Folder(),
    clipboardManager: ClipboardManager = LocalClipboardManager.current,
    scope : CoroutineScope = rememberCoroutineScope()
) {

    val snackState : SnackbarHostState = remember { SnackbarHostState() }
    var openDialog by remember { mutableStateOf(false) }

    if (openDialog) {
        FolderPathDialog(
            folder = folder,
            onCopyButtonSelected = {
                clipboardManager.setText(AnnotatedString(folder.path))
                scope.launch {
                    snackState.showSnackbar(
                        message = "Path copied",
                        duration = SnackbarDuration.Short,
                        withDismissAction = true
                    )
                }
            },
            closeDialog = { openDialog = false}
        )
    }
    Card(modifier.padding(16.dp)) {
        HorizontalDivider(Modifier.padding(start = 4.dp, end = 4.dp))
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom=8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column (Modifier.weight(0.1f)){
                Icon(Icons.Outlined.Folder, contentDescription = "")
            }
            Column(Modifier.weight(0.8f)) {
                Text(
                    modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                    text = folder.path,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(Modifier.weight(0.1f)) {
                IconButton(
                    onClick = {
                        openDialog = true
                    }
                ) {
                    Icon(Icons.Outlined.OpenInFull, "")
                }

            }
            Column(Modifier.weight(0.1f)) {
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(folder.path))
                        scope.launch {
                            snackState.showSnackbar(
                                message = "Path copied",
                                duration = SnackbarDuration.Short,
                                withDismissAction = true
                            )
                        }
                    }) {
                    Icon(Icons.Outlined.ContentCopy, "")
                }

            }
        }
        HorizontalDivider(Modifier.padding(start = 4.dp, end = 4.dp))
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom=8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Timer, contentDescription = "")

            Text(
                modifier = Modifier.padding(start = 4.dp, end = 4.dp),
                text = TimeUtils.formatTime(folder.playlist.duration),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }
        HorizontalDivider(Modifier.padding(start = 4.dp, end = 4.dp))
    }

}

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

@Preview
@Composable
private fun FolderPathDialog(folder: Folder = Folder(),
                             onCopyButtonSelected: () -> Unit = {},
                             closeDialog : () -> Unit = {}) {
    AlertDialog(
        title = { Text(folder.name + " Path") },
        confirmButton = {
            IconButton(onClick = { onCopyButtonSelected() }) {
                Icon(Icons.Outlined.ContentCopy, contentDescription = "")
            }
        },
        text = {
            Surface(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), color = MaterialTheme.colorScheme.surfaceVariant) {
                Column(
                    Modifier
                        .padding(4.dp)
                        .fillMaxSize()) {
                    Text(folder.path)
                }
            }

        },
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        dismissButton = {
            IconButton(onClick = { closeDialog() }) {
                Icon(Icons.Outlined.Close, contentDescription = "")
            }
        },
        onDismissRequest = { closeDialog() }
    )
}