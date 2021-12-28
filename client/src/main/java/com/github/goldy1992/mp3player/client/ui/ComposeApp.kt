package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.screens.FolderScreen
import com.github.goldy1992.mp3player.client.ui.screens.LibraryScreen
import com.github.goldy1992.mp3player.client.ui.screens.main.MainScreen
import com.github.goldy1992.mp3player.client.viewmodels.LibraryScreenViewModel
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.InternalCoroutinesApi


@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@InternalCoroutinesApi
@ExperimentalPagerApi
@Composable
fun ComposeApp(
        mediaBrowserAdapter: MediaBrowserAdapter,
        mediaControllerAdapter: MediaControllerAdapter,
        userPreferencesRepository: UserPreferencesRepository,
        windowSize: WindowSize,
        startScreen : Screen
) {
    val navController = rememberNavController()
    AppTheme(userPreferencesRepository = userPreferencesRepository) {
        ProvideWindowInsets {
            NavHost(
                navController = navController,
                startDestination = startScreen.name
            ) {
                composable(Screen.MAIN.name) {
                    MainScreen(
                        navController,
                        windowSize = windowSize,
                     //   mediaRepository = null,
                        mediaController = mediaControllerAdapter,
                        mediaBrowserAdapter = mediaBrowserAdapter
                    )
                }
                composable(Screen.LIBRARY.name) {
                    val viewModel = hiltViewModel<LibraryScreenViewModel>()
                    LibraryScreen(
                        navController = navController,
                        viewModel = viewModel
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
                        mediaRepository = null
                    )
                }
                composable(Screen.FOLDER.name) {
                    FolderScreen(
                       // folder = mediaRepository.currentFolder!!,
                        folder = MediaItemUtils.getEmptyMediaItem(),
                        navController = navController,
                        mediaBrowser = mediaBrowserAdapter,
                        mediaController = mediaControllerAdapter
                    )

                }
                composable(Screen.SETTINGS.name) {
                    SettingsScreen(
                        navController = navController,
                        userPreferencesRepository = userPreferencesRepository
                    )
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
                        userPreferencesRepository = userPreferencesRepository
                    )
                }
            }
        }
    }
}
