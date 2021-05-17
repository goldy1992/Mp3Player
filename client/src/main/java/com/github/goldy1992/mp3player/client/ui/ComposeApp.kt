package com.github.goldy1992.mp3player.client.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.google.accompanist.pager.ExperimentalPagerApi



@ExperimentalPagerApi
@Composable
fun ComposeApp(mediaRepository: MediaRepository,
        mediaBrowserAdapter: MediaBrowserAdapter,
        mediaControllerAdapter: MediaControllerAdapter) {
    val navController = rememberNavController()
    NavHost(
        navController =  navController,
        startDestination = "mainScreen") {
        composable("mainScreen") { mainScreen(navController,
            mediaRepository = mediaRepository,
            mediaController = mediaControllerAdapter) }
        composable("mediaPlayer") { }
    }
}