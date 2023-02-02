package com.github.goldy1992.mp3player.client.ui.screens.album

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.data.Album
import com.github.goldy1992.mp3player.client.data.Songs
import com.github.goldy1992.mp3player.client.ui.WindowSize
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.lists.songs.AlbumArt
import com.github.goldy1992.mp3player.client.utils.SongUtils
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalCoilApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun AlbumScreen(
    navController : NavController = rememberAnimatedNavController(),
    viewModel: AlbumScreenViewModel = viewModel(),
    windowSize: WindowSize = WindowSize.Compact,
    scope : CoroutineScope = rememberCoroutineScope()
) {

    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentSong by viewModel.currentMediaItem.collectAsState()
    val album : Album by viewModel.albumState.collectAsState()

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

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val albumSongs = album.songs.songs
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = bottomBar,
        topBar = {
            AlbumAppBar(
                albumProvider = { album },
                navController = navController,
                scrollBehavior =  scrollBehavior,
                scope = scope
            )
        }
    ) { paddingValues ->
        LazyColumn(
            userScrollEnabled = true,
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(count = albumSongs.size + 1) { currentAlbumSongIndex ->
                if (currentAlbumSongIndex == 0) {
                    Column(
                        Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card {
                            AlbumArt(uri = album.albumArt, modifier = Modifier.size(200.dp))
                        }

                    }
                } else {
                    val albumSongIndex = currentAlbumSongIndex - 1
                    val albumSong = albumSongs[albumSongIndex]
                    val isSongSelected = SongUtils.isSongItemSelected(albumSong, currentSong)
                    val containerColor : Color = if (isSongSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                    ListItem(
                        modifier = Modifier.combinedClickable(
                            onClick = { viewModel.playFromSongList(albumSongIndex, album.songs) },
                            onLongClick = { /* TODO: Add a long click */}
                        ),
                        colors = ListItemDefaults.colors(containerColor = containerColor),
                        leadingContent = { Text("$currentAlbumSongIndex") },
                        headlineText = {
                            Text(
                                albumSong.title,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        supportingText = {
                            Text(
                                albumSong.artist,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    )
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Preview
@Composable
fun AlbumAppBar(modifier: Modifier = Modifier,
                albumProvider: () -> Album = {Album() },
                scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                scope: CoroutineScope = rememberCoroutineScope(),
                navController: NavController = rememberAnimatedNavController()) {
    val album = albumProvider()
    LargeTopAppBar(
        title = {
            Text(album.albumTitle            )
        },
        navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        Icons.Filled.ArrowBack, "Back",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }

        },
        colors = TopAppBarDefaults.largeTopAppBarColors(),
        scrollBehavior = scrollBehavior)
}