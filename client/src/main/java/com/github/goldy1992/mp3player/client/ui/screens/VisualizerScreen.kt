package com.github.goldy1992.mp3player.client.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.ui.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import com.github.goldy1992.mp3player.client.ui.components.equalizer.cards.BarCard
import com.github.goldy1992.mp3player.client.ui.components.equalizer.cards.SmoothLineCard
import com.github.goldy1992.mp3player.client.ui.components.equalizer.fireworks.FireworkWrapper
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VisualizerScreen(
    navController : NavController = rememberAnimatedNavController(),
    viewModel: VisualizerViewModel = viewModel(),
    scope : CoroutineScope = rememberCoroutineScope()) {

    val audioMagnitudes by viewModel.audioDataState.collectAsState()
    val mediaControllerAdapter = viewModel.mediaControllerAdapter

    Scaffold(
        topBar = {
            TopBar {
                NavUpButton(
                    navController = navController,
                    scope = scope
                )
            }
         },
        bottomBar = {
            PlayToolbar(
                mediaControllerAdapter = mediaControllerAdapter,
                isPlayingState = viewModel.isPlaying.state,
                scope = scope,
                navController = navController
            )
        },
    ) {

//        VisualizerContent(
//            modifier = Modifier.padding(it),
//       //     audioMagnitudes = audioMagnitudes,
//            isPlaying = viewModel.isPlaying.state,
//            scope = scope
//        )
        VisualizerContentCardCollection(modifier = Modifier.padding(it),
                audioMagnitudes = {audioMagnitudes})
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TopBar(navIcon: @Composable () -> Unit = {}) {
    TopAppBar(
       title = {
           Text(text = "Equalizer",
               style = MaterialTheme.typography.titleLarge,
               color = MaterialTheme.colorScheme.onSurface,
               modifier = Modifier.semantics {
                contentDescription = "Equalizer"
               }
           )

        },
        navigationIcon = navIcon,
        actions = {},
        windowInsets = TopAppBarDefaults.windowInsets
    )
}


@OptIn(ExperimentalAnimationApi::class)
@Preview(showBackground = true)
@Composable
private fun VisualizerContent(
    modifier: Modifier = Modifier,
    audioMagnitudes : FloatArray = FloatArray(5),
    isPlaying : StateFlow<Boolean> = MutableStateFlow(true),
    scope: CoroutineScope = rememberCoroutineScope()) {

        Surface(
            modifier = modifier
                .fillMaxSize()) {
            val spaceBetweenBars = 5f
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                var selectedTab by remember { mutableStateOf(EqualizerType.BAR) }
                TabRow(selectedTabIndex = when(selectedTab) {
                    EqualizerType.BAR -> 0
                    EqualizerType.FIREWORK -> 1
                    EqualizerType.LINE -> 2
                }) {
                    Tab(
                        selected = selectedTab == EqualizerType.BAR,
                        onClick = { selectedTab = EqualizerType.BAR }) {
                        Text("Bar")
                    }
                    Tab(
                        selected = selectedTab == EqualizerType.FIREWORK,
                        onClick = { selectedTab = EqualizerType.FIREWORK }) {
                        Text("Firework")
                    }
                    Tab(
                        selected = selectedTab == EqualizerType.LINE,
                        onClick = { selectedTab = EqualizerType.LINE }) {
                        Text("Line")
                    }
                }

                Column {
                    AnimatedContent(targetState = selectedTab) {
                        targetState ->
                        when(targetState) {
                            EqualizerType.BAR -> {
                                Column(Modifier.fillMaxSize()) {
//                                    BarEqualizer(
//                                        modifier = modifier
//                                            .padding(10.dp)
//                                            .fillMaxWidth(),
//                                        bars = audioMagnitudes,
//                                        spaceBetweenBars = spaceBetweenBars
//                                    )
                                }
                            }
                            EqualizerType.FIREWORK -> {
                                FireworkWrapper(frequencyPhases = audioMagnitudes.asList(), insetPx = 50f, isPlayingState = isPlaying)
                            }
                            EqualizerType.LINE -> {
//                                SmoothLineEqualizer(
//                                    modifier = modifier,
//                            //        frequencyPhasesState = audioMagnitudes,
//                                    scope = scope
//                                )
                            }
                        }
                    }
                }
                // BarEqualizer(bars = audioMagnitudes)
                // AnimatedEqualizer(modifier = Modifier.fillMaxSize())
                // LineEqualizerWithStateListCanvasOnly(frequencyPhases = frequencyPhases.asList())
            }

        }
    }

@Preview
@Composable
fun VisualizerContentCardCollection(
    modifier: Modifier = Modifier,
    audioMagnitudes : () -> List<Float> = { listOf(100f, 200f, 300f, 150f)},
    scope: CoroutineScope = rememberCoroutineScope()) {

    LazyColumn(modifier.fillMaxSize()) {
        item {
            BarCard(
                modifier = Modifier.width(200.dp).height(200.dp),
                frequencyValues = audioMagnitudes,
                scope = scope
            )
        }
        item {
            SmoothLineCard(
                modifier = Modifier.width(200.dp).height(200.dp),
                frequencyPhases = audioMagnitudes,
                scope = scope
            )
        }
    }

}

enum class EqualizerType {
    BAR,
    FIREWORK,
    LINE
}





