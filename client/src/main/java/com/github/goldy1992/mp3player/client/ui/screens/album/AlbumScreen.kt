package com.github.goldy1992.mp3player.client.ui.screens.album

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
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
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.data.PlaybackState
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.ui.buttons.AlbumPlayPauseButton
import com.github.goldy1992.mp3player.client.ui.buttons.ShuffleButton
import com.github.goldy1992.mp3player.client.ui.components.AlbumArtAsync
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.utils.SongUtils
import com.github.goldy1992.mp3player.client.utils.TimeUtils
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch



@Composable
fun SharedTransitionScope.AlbumScreen(
    navController: NavController = rememberNavController(),
    viewModel: AlbumScreenViewModel = viewModel(),
    scope: CoroutineScope = rememberCoroutineScope(),
    animatedContentScope: AnimatedContentScope

) {

    val isPlaying by viewModel.isPlaying.state().collectAsState()
    val isShuffleModeEnabled by viewModel.shuffleEnabled.state().collectAsState()
    val currentSong by viewModel.currentSong.state().collectAsState()
    val album : Album by viewModel.albumState.collectAsState()
    val currentPlaylistId : String by viewModel.currentPlaylistIdState.collectAsState()
    val playbackState = PlaybackState(
        isPlayingProvider = {isPlaying},
        currentSongProvider = {currentSong},
        onClickPlay = { viewModel.play() },
        onClickPause = {viewModel.pause() },
        onClickSkipPrevious = { viewModel.skipToPrevious() },
        onClickSkipNext = { viewModel.skipToNext() }
    )
    val albumPlayPauseButton : @Composable () -> Unit = {
        AlbumPlayPauseButton(isPlaying = { isAlbumPlaying(isPlaying, currentPlaylistId, album) },
                            onClickPlay = {
                                if (isCurrentPlaylistTheSelectedAlbum(currentPlaylistId, album)) {
                                    viewModel.play()
                                } else {
                                    viewModel.playAlbum(0, album)
                                }
                            },
                            onClickPause = { viewModel.pause()},
                            modifier = Modifier.size(40.dp))
    }
    val shuffleButton : @Composable () -> Unit = {
        ShuffleButton(
            modifier = Modifier.size(40.dp),
            shuffleEnabledProvider = { isShuffleModeEnabled },
            onClick = { viewModel.setShuffleEnabled(isShuffleModeEnabled)}
        )
    }
    val onAlbumSongSelected : (Int) -> Unit = { itemIndex ->
        viewModel.playAlbum(itemIndex, album)
    }
    val bottomBar : @Composable () -> Unit = {
        PlayToolbar(
            animatedVisibilityScope = animatedContentScope,
            playbackState = playbackState,
            onClickBar = { navController.navigate(Screen.NOW_PLAYING.name)},
        )
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val albumSongs = album.playlist.songs
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
                    AlbumHeaderItem(
                        albumProvider = { album},
                        albumPlayPauseButton = albumPlayPauseButton,
                        shuffleButton = shuffleButton
                    )
                } else {
                    val albumSongIndex = currentAlbumSongIndex - 1
                    val albumSong = albumSongs[albumSongIndex]
                    val isSongSelected = SongUtils.isSongItemSelected(albumSong, currentSong)
                    val containerColor : Color = if (isSongSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                    ListItem(
                        modifier = Modifier.combinedClickable(
                            onClick = { onAlbumSongSelected(albumSongIndex) },
                            onLongClick = { /* TODO: Add a long click */}
                        ),
                        colors = ListItemDefaults.colors(containerColor = containerColor),
                        leadingContent = { Text("$currentAlbumSongIndex") },
                        headlineContent = { Text(albumSong.title) },
                        supportingContent = { Text(albumSong.artist) }
                    )
                }
            }
        }
    }

}

private fun isAlbumPlaying(
    isPlaying: Boolean,
    currentPlaylistId: String,
    album: Album
) = isPlaying && isCurrentPlaylistTheSelectedAlbum(currentPlaylistId, album)

@Preview
@Composable
private fun AlbumHeaderItem(
    albumProvider: () -> Album = { Album() },
    onClickShuffle : () -> Unit = {},
    albumPlayPauseButton : @Composable () -> Unit = { AlbumPlayPauseButton(modifier = Modifier.size(50.dp))},
    shuffleButton : @Composable () -> Unit = { ShuffleButton(modifier = Modifier.size(50.dp))}) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val album = albumProvider()
        Card {
            AlbumArtAsync(uri = album.artworkUri, contentDescription = album.title, modifier = Modifier.size(200.dp))
        }

        Column(
            Modifier.padding(7.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = album.artist,
                style = MaterialTheme.typography.labelLarge
            )
            val singularSong = "song"
            val pluralSongs = "songs"
            val numOfSongs = album.playlist.songs.size
            val songDescr = if (numOfSongs == 1) singularSong else pluralSongs
            val duration = TimeUtils.formatTime(album.playlist.duration)
            val summary = "$numOfSongs $songDescr | $duration"
            Text(
                text = summary,
                style = MaterialTheme.typography.labelMedium
            )

            Row(modifier = Modifier
                .padding(top = 7.dp)
                .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically) {
                shuffleButton()
                Spacer(modifier = Modifier.width(12.dp))
                HorizontalDivider(modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                    thickness = 1.dp)
                Spacer(modifier = Modifier.width(12.dp))

                albumPlayPauseButton()
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Preview
@Composable
fun AlbumAppBar(modifier: Modifier = Modifier,
                albumProvider: () -> Album = { Album() },
                scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
                scope: CoroutineScope = rememberCoroutineScope(),
                navController: NavController = rememberNavController()
) {
    val album = albumProvider()
    LargeTopAppBar(
        modifier = modifier,
        title = {
            Text(album.title)
        },
        navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack, "Back",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }

        },
        colors = TopAppBarDefaults.largeTopAppBarColors(),
        scrollBehavior = scrollBehavior)
}

private fun isCurrentPlaylistTheSelectedAlbum(currentPlaylistId : String, album: Album) : Boolean {
    return currentPlaylistId == album.id
}