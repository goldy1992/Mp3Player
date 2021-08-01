package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.InternalCoroutinesApi


@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@InternalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun ComposeApp(mediaRepository: MediaRepository,
        mediaBrowserAdapter: MediaBrowserAdapter,
        mediaControllerAdapter: MediaControllerAdapter,
        userPreferencesRepository: UserPreferencesRepository,
        startScreen : Screen) {
    val navController = rememberNavController()
    AppTheme(userPreferencesRepository = userPreferencesRepository) {
        NavHost(
            navController = navController,
            startDestination = startScreen.name
        ) {
            composable(Screen.MAIN.name) {
                MainScreen(
                    navController,
                    mediaRepository = mediaRepository,
                    mediaController = mediaControllerAdapter
                )
            }
            composable(Screen.NOW_PLAYING.name) {
                NowPlayingScreen(
                    navController = navController,
                    mediaController = mediaControllerAdapter
                )
            }
            composable(Screen.SEARCH.name) {
                SearchScreen(
                    navController = navController,
                    mediaBrowser = mediaBrowserAdapter,
                    mediaController = mediaControllerAdapter,
                    mediaRepository = mediaRepository
                )
            }
            composable(Screen.FOLDER.name) {
                FolderScreen(
                        folder = mediaRepository.currentFolder!!,
                        navController = navController,
                        mediaBrowser = mediaBrowserAdapter,
                        mediaController = mediaControllerAdapter
                )

            }
            composable(Screen.SETTINGS.name) {
                SettingsScreen(navController = navController,
                    userPreferencesRepository = userPreferencesRepository)
            }
            composable(Screen.THEME_SELECT.name) {
                ThemeSelectScreen(
                    navController = navController,
                    userPreferencesRepository = userPreferencesRepository
                )
            }
            composable(Screen.SPLASH.name) {
                SplashScreen(
                    navController = navController,
                    userPreferencesRepository = userPreferencesRepository)
            }
        }
    }
}
