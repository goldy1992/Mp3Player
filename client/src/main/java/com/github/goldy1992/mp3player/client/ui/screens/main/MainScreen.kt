package com.github.goldy1992.mp3player.client.ui.screens.main

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

/**
 * The Main Screen of the app.
 *
 * @param navController The [NavController].
 * @param windowSize The [WindowSize] object.
 * @param viewModel The [MainScreenViewModel].
 */
@ExperimentalMaterialApi
@Composable
fun MainScreen(navController: NavController,
               windowSize: WindowSizeClass,
               viewModel: MainScreenViewModel = viewModel(),
) {

    Text("Welcome to the main screen")
}
