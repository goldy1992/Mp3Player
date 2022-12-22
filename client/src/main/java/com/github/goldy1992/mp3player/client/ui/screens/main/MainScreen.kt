package com.github.goldy1992.mp3player.client.ui.screens.main

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.ui.WindowSize
import com.google.accompanist.pager.ExperimentalPagerApi

/**
 * The Main Screen of the app.
 *
 * @param navController The [NavController].
 * @param windowSize The [WindowSize] object.
 * @param viewModel The [MainScreenViewModel].
 */
@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun MainScreen(navController: NavController,
               windowSize: WindowSize,
               viewModel: MainScreenViewModel = viewModel(),
) {

    Text("Welcome to the main screen")
}
