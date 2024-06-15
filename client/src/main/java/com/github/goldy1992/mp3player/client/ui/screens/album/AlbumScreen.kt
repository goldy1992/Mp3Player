package com.github.goldy1992.mp3player.client.ui.screens.album

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
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
import com.github.goldy1992.mp3player.client.models.media.PlaybackState
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
    animatedContentScope: AnimatedVisibilityScope

) {
    val playbackState by viewModel.playbackState.collectAsState()
    val album : Album by viewModel.albumState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val albumSongs = album.playlist.songs
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        bottomBar = {
            PlayToolbar(
                animatedVisibilityScope = animatedContentScope,
                playbackState = playbackState,
                onClickBar = { navController.navigate(Screen.NOW_PLAYING.name)},
            )
        },
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
                        animatedVisibilityScope = animatedContentScope,
                        playbackStateProvider = { playbackState},
                        albumProvider = { album},
                    )
                } else {
                    val albumSongIndex = currentAlbumSongIndex - 1
                    val albumSong = albumSongs[albumSongIndex]
                    val isSongSelected = SongUtils.isSongItemSelected(albumSong, playbackState.currentSong)
                    val containerColor : Color = if (isSongSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                    ListItem(
                        modifier = Modifier.combinedClickable(
                            onClick = { playbackState.actions.onAlbumSongSelected(albumSongIndex, album) },
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


@Preview
@Composable
private fun SharedTransitionScope.AlbumHeaderItem(
    animatedVisibilityScope: AnimatedVisibilityScope,
    playbackStateProvider : () ->  PlaybackState = {PlaybackState.DEFAULT},
    albumProvider: () -> Album = { Album() },
    currentPlaylistIdProvider : () -> String = {""},
    onClickShuffle : () -> Unit = {},
){
    Column(
        Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val album = albumProvider()
        val playbackState = playbackStateProvider()
        val actions = playbackState.actions
        Card {
            AlbumArtAsync(
                uri = album.artworkUri,
                contentDescription = album.title,
                modifier = Modifier
                .sharedElement(
                    rememberSharedContentState(key = album.id),
                    animatedVisibilityScope = animatedVisibilityScope
                ).size(200.dp))
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

                ShuffleButton(
                    modifier = Modifier.size(40.dp),
                    isShuffleEnabled = playbackState.shuffleEnabled,
                    onClick = { playbackState.actions.setShuffleEnabled(!playbackState.shuffleEnabled)}
                )

                Spacer(modifier = Modifier.width(12.dp))
                HorizontalDivider(modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp),
                    thickness = 1.dp)
                Spacer(modifier = Modifier.width(12.dp))

                val currentPlaylistId = currentPlaylistIdProvider()

                AlbumPlayPauseButton(isPlaying = {actions.isAlbumPlaying(playbackState.isPlaying, currentPlaylistId, album)},
                    onClickPlay = {
                        actions.onPlayAlbum(currentPlaylistId, album)
                    },
                    onClickPause = actions.pause,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}


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
