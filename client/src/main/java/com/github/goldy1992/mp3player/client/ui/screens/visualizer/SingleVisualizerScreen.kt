package com.github.goldy1992.mp3player.client.ui.screens.visualizer

import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.goldy1992.mp3player.client.ui.DpPxSize
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.equalizer.VisualizerType
import com.github.goldy1992.mp3player.client.ui.components.equalizer.bar.BarEqualizer
import com.github.goldy1992.mp3player.client.ui.components.equalizer.circular.CircleEqualizerUsingPolikarpotchkin
import com.github.goldy1992.mp3player.client.ui.components.equalizer.fountainspring.FountainSpringVisualizer
import com.github.goldy1992.mp3player.client.ui.components.equalizer.pichart.PieChartVisualizer
import com.github.goldy1992.mp3player.client.ui.components.equalizer.smoothline.SmoothLineEqualizer
import com.github.goldy1992.mp3player.client.ui.screens.visualizer.VisualizerUtils.getVisualizerName
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope

private const val LOG_TAG = "SingleVisualizerScreen"


@Composable
fun SharedTransitionScope.SingleVisualizerScreen(
    navController: NavController = rememberNavController(),
    viewModel: SingleVisualizerScreenViewModel = viewModel(),
    scope : CoroutineScope = rememberCoroutineScope(),
    animatedContentScope: AnimatedContentScope

) {
    val isPlaying by viewModel.isPlaying.state().collectAsState()
    val audioData by viewModel.audioData.state().collectAsState()
    val visualizerName = getVisualizerName(viewModel.visualizer, LocalContext.current)
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(visualizerName) },
                navigationIcon = {
                    NavUpButton(navController = navController, scope = scope)
                })
        },
        bottomBar = {
            PlayToolbar(
                animatedVisibilityScope = animatedContentScope,
                isPlayingProvider = { isPlaying },
                onClickPlay = { viewModel.play() },
                onClickPause = {viewModel.pause() },
                onClickSkipPrevious = { viewModel.skipToPrevious() },
                onClickSkipNext = { viewModel.skipToNext() },
                onClickBar = { navController.navigate(Screen.NOW_PLAYING.name)}
            )

        }
    ) {
        val density = LocalDensity.current
        var canvasSize by remember{mutableStateOf(DpPxSize.DEFAULT)}
        Column(
            Modifier
                .padding(it)
                .onSizeChanged { newCanvasSize ->
                    canvasSize = DpPxSize.createDpPxSizeFromPx(
                        widthPx = newCanvasSize.width.toFloat(),
                        heightPx = newCanvasSize.height.toFloat(),
                        density = density
                    )
                }) {

            when(viewModel.visualizer) {
                VisualizerType.BAR -> {
                    BarEqualizer(
                        canvasSize = canvasSize,
                        frequencyValues = { audioData },
                        animatedVisibilityScope = animatedContentScope
                    )
                }
                VisualizerType.LINE -> {
                    SmoothLineEqualizer(
                        frequencyPhasesState = { audioData },
                        canvasSize = canvasSize,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(VisualizerType.LINE),
                            animatedVisibilityScope = animatedContentScope
                        ),
                    )
                }
                VisualizerType.FOUNTAIN -> {
                    FountainSpringVisualizer(
                        frequencyPhasesProvider = {audioData},
                        canvasSize = canvasSize,
                        isPlayingProvider = { isPlaying },
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(VisualizerType.FOUNTAIN),
                            animatedVisibilityScope = animatedContentScope
                        ),
                    )
                }
                VisualizerType.CIRCULAR -> {
                    CircleEqualizerUsingPolikarpotchkin(
                        frequencyPhasesState = { audioData },
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(VisualizerType.CIRCULAR),
                            animatedVisibilityScope = animatedContentScope
                        ),
                    )
                }
                VisualizerType.PIE_CHART -> {
                    PieChartVisualizer(
                        frequencyPhasesState = { audioData },
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(VisualizerType.PIE_CHART),
                            animatedVisibilityScope = animatedContentScope
                        ),
                    )
                }
                else -> {
                    Log.w(LOG_TAG, "Unrecognised Visualizer Type")
                }
            }
        }
    }
}