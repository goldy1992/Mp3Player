package com.github.goldy1992.mp3player.client.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.InternalCoroutinesApi


@InternalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun ComposeApp(mediaRepository: MediaRepository,
        mediaBrowserAdapter: MediaBrowserAdapter,
        mediaControllerAdapter: MediaControllerAdapter) {
    val navController = rememberNavController()
    AppTheme {
        NavHost(
            navController = navController,
            startDestination = MAIN_SCREEN
        ) {
            composable(MAIN_SCREEN) {
                MainScreen(
                    navController,
                    mediaRepository = mediaRepository,
                    mediaController = mediaControllerAdapter
                )
            }
            composable(NOW_PLAYING_SCREEN) {
                NowPlayingScreen(
                    navController = navController,
                    mediaController = mediaControllerAdapter
                )
            }
            composable(SEARCH_SCREEN) {
                SearchScreen(
                    navController = navController,
                    mediaBrowser = mediaBrowserAdapter,
                    mediaController = mediaControllerAdapter
                )
            }
        }
    }
}