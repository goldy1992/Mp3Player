package com.github.goldy1992.mp3player.client.ui

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDeepLink
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.screens.FolderScreen
import com.github.goldy1992.mp3player.client.ui.screens.library.LibraryScreen
import com.github.goldy1992.mp3player.client.ui.screens.main.MainScreen
import com.github.goldy1992.mp3player.client.viewmodels.*
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.InternalCoroutinesApi


private const val logTag = "ComposeApp"

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
        NavHost(
            navController = navController,
            startDestination = Screen.LIBRARY.name
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
            composable(Screen.NOW_PLAYING.name,
                        deepLinks = listOf<NavDeepLink>(navDeepLink {
                            uriPattern = "com.github.goldy1992.mp3player/${Screen.NOW_PLAYING.name}"
                            action = Intent.ACTION_VIEW })
            ) {
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
        Log.i(logTag, "hit this line")
    }

}
