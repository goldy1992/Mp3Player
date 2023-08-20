package com.github.goldy1992.mp3player.client.ui.screens.visualizer

import CircleEqualizerUsingMulkit
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
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
import com.github.goldy1992.mp3player.client.ui.DpPxSize
import com.github.goldy1992.mp3player.client.ui.buttons.NavUpButton
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.equalizer.VisualizerType
import com.github.goldy1992.mp3player.client.ui.components.equalizer.bar.BarEqualizer
import com.github.goldy1992.mp3player.client.ui.components.equalizer.circular.CircularEqualizer
import com.github.goldy1992.mp3player.client.ui.components.equalizer.circular.CircularEqualizerInscapeImpl
import com.github.goldy1992.mp3player.client.ui.components.equalizer.circular.CircularEqualizerNewImpl
import com.github.goldy1992.mp3player.client.ui.components.equalizer.fountainspring.FountainSpringVisualizer
import com.github.goldy1992.mp3player.client.ui.components.equalizer.pichart.PieChartVisualizer
import com.github.goldy1992.mp3player.client.ui.components.equalizer.smoothline.SmoothLineEqualizer
import com.github.goldy1992.mp3player.client.ui.screens.visualizer.VisualizerUtils.getVisualizerName
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope

private const val LOG_TAG = "SingleVisualizerScreen"

@OptIn(ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SingleVisualizerScreen(
    navController: NavController = rememberAnimatedNavController(),
    viewModel: SingleVisualizerScreenViewModel = viewModel(),
    scope : CoroutineScope = rememberCoroutineScope()
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
                        frequencyValues = { audioData }
                    )
                }
                VisualizerType.LINE -> {
                    SmoothLineEqualizer(
                        frequencyPhasesState = { audioData },
                        canvasSize = canvasSize,
                    )
                }
                VisualizerType.FOUNTAIN -> {
                    FountainSpringVisualizer(
                        frequencyPhasesProvider = {audioData},
                        canvasSize = canvasSize,
                        isPlayingProvider = { isPlaying }
                    )
                }
                VisualizerType.CIRCULAR -> {
                    CircleEqualizerUsingMulkit(
                        frequencyPhasesState = { audioData },
                        canvasSize = canvasSize,
                    )
                }
                VisualizerType.PIE_CHART -> {
                    PieChartVisualizer(
                        frequencyPhasesState = { audioData },
                        canvasSize = canvasSize,
                    )
                }
                else -> {
                    Log.w(LOG_TAG, "Unrecognised Visualizer Type")
                }
            }
        }
    }
}