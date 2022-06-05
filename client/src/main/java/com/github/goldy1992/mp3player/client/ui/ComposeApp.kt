package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.screens.FolderScreen
import com.github.goldy1992.mp3player.client.ui.screens.VisualizerScreen
import com.github.goldy1992.mp3player.client.ui.screens.VisualizerViewModel
import com.github.goldy1992.mp3player.client.ui.screens.library.LibraryScreen
import com.github.goldy1992.mp3player.client.ui.screens.main.MainScreen
import com.github.goldy1992.mp3player.client.viewmodels.LibraryScreenViewModel
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
                        mediaController = mediaControllerAdapter,
                        mediaBrowserAdapter = mediaBrowserAdapter
                    )
                }
                composable(Screen.LIBRARY.name) {
                    val viewModel = hiltViewModel<LibraryScreenViewModel>()
                    LibraryScreen(
                        navController = navController,
                        viewModel = viewModel,
                        windowSize = windowSize
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
                        windowSize = windowSize
                    )
                }
                composable(
                    route = Screen.FOLDER.name + "/{folderId}/{folderName}/{folderPath}",
                    arguments = listOf(
                        navArgument("folderId") {type = NavType.StringType},
                        navArgument("folderName") {type = NavType.StringType},
                        navArgument("folderPath") {type = NavType.StringType}
                )) {
                    FolderScreen(
                        folderId = it.arguments?.get("folderId") as String,
                        folderName = it.arguments?.get("folderName") as String,
                        folderPath = it.arguments?.get("folderPath") as String,
                        navController = navController,
                        mediaBrowser = mediaBrowserAdapter,
                        mediaController = mediaControllerAdapter,
                        windowSize = windowSize
                    )

                }
                composable(Screen.SETTINGS.name) {
                    SettingsScreen(
                        navController = navController,
                        userPreferencesRepository = userPreferencesRepository,
                        windowSize = windowSize
                    )
                }
                composable(Screen.VISUALIZER.name){
                    val viewModel = hiltViewModel<VisualizerViewModel>()
                    VisualizerScreen()
                }
            }
        }
    }
}
