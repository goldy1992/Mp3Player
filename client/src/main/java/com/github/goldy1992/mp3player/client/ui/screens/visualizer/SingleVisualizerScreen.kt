package com.github.goldy1992.mp3player.client.ui.screens.visualizer

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope


@OptIn(ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class)
@Composable
fun SingleVisualizerScreen(
    navController: NavController = rememberAnimatedNavController(),
    viewModel: SingleVisualizerScreenViewModel = viewModel(),
    scope : CoroutineScope = rememberCoroutineScope()
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("") },
                navigationIcon = {
                    NavUpButton(navController = navController, scope = scope)
                })
        }
    ) {
        Column(Modifier.padding(it)) {

        }
    }
}