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
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.screens.FolderScreen
import com.github.goldy1992.mp3player.client.ui.screens.library.LibraryScreen
import com.github.goldy1992.mp3player.client.ui.screens.main.MainScreen
import com.github.goldy1992.mp3player.client.viewmodels.*
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
                    val viewModel = hiltViewModel<MainScreenViewModel>()
                    MainScreen(
                        navController,
                        windowSize = windowSize,
                        viewModel = viewModel
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
                    val viewModel = hiltViewModel<NowPlayingScreenViewModel>()
                    NowPlayingScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(Screen.SEARCH.name) {
                    val viewModel = hiltViewModel<SearchScreenViewModel>()
                    SearchScreen(
                        navController = navController,
                        windowSize = windowSize,
                        viewModel = viewModel
                    )
                }
                composable(
                    route = Screen.FOLDER.name + "/{folderId}/{folderName}/{folderPath}",
                    arguments = listOf(
                        navArgument("folderId") {type = NavType.StringType},
                        navArgument("folderName") {type = NavType.StringType},
                        navArgument("folderPath") {type = NavType.StringType}
                )) {
                    val viewModel = hiltViewModel<FolderScreenViewModel>()
                    FolderScreen(
                        folderId = it.arguments?.get("folderId") as String,
                        folderName = it.arguments?.get("folderName") as String,
                        folderPath = it.arguments?.get("folderPath") as String,
                        navController = navController,
                        windowSize = windowSize,
                        viewModel = viewModel
                    )

                }
                composable(Screen.SETTINGS.name) {
                    SettingsScreen(
                        navController = navController,
                        userPreferencesRepository = userPreferencesRepository,
                        windowSize = windowSize
                    )
                }
            }
        }
    }
}
