package com.github.goldy1992.mp3player.client.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferencesRepository
import com.github.goldy1992.mp3player.client.ui.screens.FolderScreen
import com.github.goldy1992.mp3player.client.ui.screens.FolderScreenViewModel
import com.github.goldy1992.mp3player.client.ui.screens.album.AlbumScreen
import com.github.goldy1992.mp3player.client.ui.screens.album.AlbumScreenViewModel
import com.github.goldy1992.mp3player.client.ui.screens.library.LibraryScreen
import com.github.goldy1992.mp3player.client.ui.screens.library.LibraryScreenViewModel
import com.github.goldy1992.mp3player.client.ui.screens.main.MainScreen
import com.github.goldy1992.mp3player.client.ui.screens.main.MainScreenViewModel
import com.github.goldy1992.mp3player.client.ui.screens.nowplaying.NowPlayingScreen
import com.github.goldy1992.mp3player.client.ui.screens.nowplaying.NowPlayingScreenViewModel
import com.github.goldy1992.mp3player.client.ui.screens.search.SearchScreen
import com.github.goldy1992.mp3player.client.ui.screens.search.SearchScreenViewModel
import com.github.goldy1992.mp3player.client.ui.screens.settings.SettingsScreen
import com.github.goldy1992.mp3player.client.ui.screens.settings.SettingsScreenViewModel
import com.github.goldy1992.mp3player.client.ui.screens.visualizer.VisualizerScreen
import com.github.goldy1992.mp3player.client.ui.screens.visualizer.VisualizerViewModel
import com.github.goldy1992.mp3player.commons.Constants.ROOT_APP_URI_PATH
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.InternalCoroutinesApi


private const val logTag = "ComposeApp"
private const val transitionTime = 2000
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalPagerApi::class,
    InternalCoroutinesApi::class,
)
@Composable
fun ComposeApp(
    userPreferencesRepository: IUserPreferencesRepository,
    windowSize: WindowSize,
    startScreen : Screen
) {
    val navController = rememberAnimatedNavController()
    AppTheme(userPreferencesRepository = userPreferencesRepository) {
        AnimatedNavHost(
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
                enterTransition = {
                    Log.i(logTag, "enterTransition called")
                    slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Up, animationSpec = tween(transitionTime)
                    )
                },
                popEnterTransition = {
                    Log.i(logTag, "PopenterTransition called")
                    slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Up, animationSpec = tween(transitionTime)
                    )
                },
                exitTransition = {
                    Log.i(logTag, "exit called")
                   slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(transitionTime))
                },
                popExitTransition = {
                    Log.i(logTag, "pop exitTransition called")
                    slideOutOfContainer(AnimatedContentScope.SlideDirection.Down, animationSpec = tween(transitionTime))
                },
                        deepLinks = listOf(navDeepLink {
                            uriPattern = "${ROOT_APP_URI_PATH}/${Screen.NOW_PLAYING.name}"
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
            composable(
                route = Screen.ALBUM.name + "/{albumId}/{albumTitle}/{albumArtist}/{albumArtUri}",
                arguments = listOf(
                    navArgument("albumId") { type = NavType.StringType },
                    navArgument("albumTitle") { type = NavType.StringType },
                    navArgument("albumArtist") { type = NavType.StringType },
                    navArgument("albumArtUri") { type = NavType.StringType },
                )) {
                val viewModel = hiltViewModel<AlbumScreenViewModel>()
                AlbumScreen(
                    navController = navController,
                    windowSize = windowSize,
                    viewModel = viewModel
                )

            }
                composable(Screen.SETTINGS.name) {
                    val viewModel = hiltViewModel<SettingsScreenViewModel>()
                    SettingsScreen(
                        navController = navController,
                        viewModel = viewModel,
                        windowSize = windowSize
                    )
                }
                composable(Screen.VISUALIZER.name){
                    val viewModel = hiltViewModel<VisualizerViewModel>()
                    VisualizerScreen(
                        navController = navController,
                        viewModel = viewModel)
                }
            }
        }
        Log.i(logTag, "hit this line")
    }

