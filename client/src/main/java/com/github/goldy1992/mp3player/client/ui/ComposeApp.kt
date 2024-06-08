package com.github.goldy1992.mp3player.client.ui

import android.content.Intent
import android.util.Log
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
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
import com.github.goldy1992.mp3player.client.ui.screens.visualizer.SingleVisualizerScreen
import com.github.goldy1992.mp3player.client.ui.screens.visualizer.SingleVisualizerScreenViewModel
import com.github.goldy1992.mp3player.client.ui.screens.visualizer.VisualizerCollectionScreen
import com.github.goldy1992.mp3player.client.ui.screens.visualizer.VisualizerCollectionViewModel
import com.github.goldy1992.mp3player.commons.Constants.ROOT_APP_URI_PATH
import com.github.goldy1992.mp3player.commons.Screen

private const val LOG_TAG = "ComposeApp"

/**
 * Entry point to the Compose UI.
 * @param userPreferencesRepository The [IUserPreferencesRepository].
 * @param windowSize The [WindowSizeClass].
 * @param startScreen The [Screen] to begin with.
 */
@Composable
fun ComposeApp(
    userPreferencesRepository: IUserPreferencesRepository,
    windowSize: WindowSizeClass,
    startScreen : Screen
) {

    val navController = rememberNavController()
    AppTheme(userPreferencesRepository = userPreferencesRepository) {

        SharedTransitionLayout {
            NavHost(
                navController = navController,
                startDestination = startScreen.name
            ) {

                composable(Screen.MAIN.name) {
                    val viewModel = hiltViewModel<MainScreenViewModel>()
                    MainScreen(
                        navController,
                        windowSize = windowSize,
                        viewModel = viewModel,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedContentScope = this@composable

                    )
                }
                composable(Screen.LIBRARY.name) {
                    val viewModel = hiltViewModel<LibraryScreenViewModel>()
                    LibraryScreen(
                        navController = navController,
                        viewModel = viewModel,
                        windowSize = windowSize,
                        animatedContentScope = this@composable
                    )

                }
                composable(
                    Screen.NOW_PLAYING.name,
                    deepLinks = listOf(navDeepLink {
                        uriPattern = "${ROOT_APP_URI_PATH}/${Screen.NOW_PLAYING.name}"
                        action = Intent.ACTION_VIEW
                    })
                ) {
                    val viewModel = hiltViewModel<NowPlayingScreenViewModel>()
                    NowPlayingScreen(
                        navController = navController,
                        viewModel = viewModel,
                        animatedContentScope = this@composable
                    )
                }
                composable(Screen.SEARCH.name) {
                    val viewModel = hiltViewModel<SearchScreenViewModel>()
                    SearchScreen(
                        navController = navController,
                        windowSize = windowSize,
                        viewModel = viewModel,
                        animatedContentScope = this@composable
                    )
                }
                composable(
                    route = Screen.FOLDER.name + "/{folderId}/{folderName}/{folderPath}",
                    arguments = listOf(
                        navArgument("folderId") { type = NavType.StringType },
                        navArgument("folderName") { type = NavType.StringType },
                        navArgument("folderPath") { type = NavType.StringType }
                    )) {
                    val viewModel = hiltViewModel<FolderScreenViewModel>()
                    FolderScreen(
                        navController = navController,
                        windowSize = windowSize,
                        viewModel = viewModel,
                        animatedContentScope = this@composable
                    )

                }
                composable(
                    route = Screen.ALBUM.name + "/{albumId}/{albumTitle}/{albumArtist}/{albumArtUri}",
                    arguments = listOf(
                        navArgument("albumId") { type = NavType.StringType },
                        navArgument("albumTitle") { type = NavType.StringType },
                        navArgument("albumArtist") { type = NavType.StringType },
                        navArgument("albumArtUri") { type = NavType.StringType },
                    )
                ) {
                    val viewModel = hiltViewModel<AlbumScreenViewModel>()
                    AlbumScreen(
                        navController = navController,
                        viewModel = viewModel,
                        animatedContentScope = this@composable
                    )

                }
                composable(Screen.SETTINGS.name) {
                    val viewModel = hiltViewModel<SettingsScreenViewModel>()
                    SettingsScreen(
                        navController = navController,
                        viewModel = viewModel,
                        windowSize = windowSize,
                        animatedContentScope = this@composable
                    )
                }
                composable(
                    route = Screen.SINGLE_VISUALIZER.name + "/{visualizer}/{audioData}",
                    arguments = listOf(
                        navArgument("visualizer") { type = NavType.StringType },
                        navArgument("audioData") {type = NavType.StringType}
                    )
                ) {

                    val viewModel = hiltViewModel<SingleVisualizerScreenViewModel>()
                    SingleVisualizerScreen(
                        navController = navController,
                        viewModel = viewModel,
                        animatedContentScope = this@composable
                    )
                }
                composable(Screen.VISUALIZER_COLLECTION.name) {
                    val viewModel = hiltViewModel<VisualizerCollectionViewModel>()
                    VisualizerCollectionScreen(
                        navController = navController,
                        viewModel = viewModel,
                        animatedContentScope = this@composable
                    )
                }
            }
        }
    }
        Log.v(LOG_TAG, "ComposeApp() invocation complete")
}

