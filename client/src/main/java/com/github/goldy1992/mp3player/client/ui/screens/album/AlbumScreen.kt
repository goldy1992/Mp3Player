package com.github.goldy1992.mp3player.client.ui.screens.album

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.data.Album
import com.github.goldy1992.mp3player.client.data.Songs
import com.github.goldy1992.mp3player.client.ui.WindowSize
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
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
    ) {

        LazyColumn(userScrollEnabled = true,
            contentPadding = it,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(count = albumSongs.size) {
                val currentSong = albumSongs[it]
                ListItem(headlineText = {Text(currentSong.title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium)})
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Preview
@Composable
fun AlbumAppBar(modifier: Modifier = Modifier,
                albumProvider: () -> Album = {Album() },
                scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
                scope: CoroutineScope = rememberCoroutineScope(),
                navController: NavController = rememberAnimatedNavController()) {
    val album = albumProvider()
    LargeTopAppBar(
        title = {
            Text(album.albumTitle,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium)
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