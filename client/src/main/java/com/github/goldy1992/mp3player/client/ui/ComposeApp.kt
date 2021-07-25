package com.github.goldy1992.mp3player.client.ui

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


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@InternalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun ComposeApp(mediaRepository: MediaRepository,
        mediaBrowserAdapter: MediaBrowserAdapter,
        mediaControllerAdapter: MediaControllerAdapter,
        userPreferencesRepository: UserPreferencesRepository,
        showSplashScreen : Boolean = false) {
    val navController = rememberNavController()
    AppTheme(userPreferencesRepository = userPreferencesRepository) {
        NavHost(
            navController = navController,
            startDestination = getStartDestination(showSplashScreen)
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
                    mediaController = mediaControllerAdapter,
                    mediaRepository = mediaRepository
                )
            }
            composable(FOLDER_SCREEN) {
                FolderScreen(
                        folder = mediaRepository.currentFolder!!,
                        navController = navController,
                        mediaBrowser = mediaBrowserAdapter,
                        mediaController = mediaControllerAdapter
                )

            }
            composable(SETTINGS_SCREEN) {
                SettingsScreen(navController = navController,
                    userPreferencesRepository = userPreferencesRepository)
            }
            composable(THEME_SELECT_SCREEN) {
                ThemeSelectScreen(
                    navController = navController,
                    userPreferencesRepository = userPreferencesRepository
                )
            }
            composable(SPLASH_SCREEN) {
                SplashScreen(
                    navController = navController,
                    userPreferencesRepository = userPreferencesRepository)
            }
        }
    }
}

private fun getStartDestination(showSplashScreen: Boolean) : String {
    return  if (showSplashScreen)  SPLASH_SCREEN else MAIN_SCREEN
}