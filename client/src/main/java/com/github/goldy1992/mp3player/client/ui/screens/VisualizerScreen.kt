package com.github.goldy1992.mp3player.client.ui.screens

import android.widget.Toolbar
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.equalizer.fireworks.FireworkWrapper
import com.github.goldy1992.mp3player.commons.Screen
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VisualizerScreen(
    navController : NavController = rememberAnimatedNavController(),
    viewModel: VisualizerViewModel = viewModel(),
    scope : CoroutineScope = rememberCoroutineScope()) {

    //val audioSample by viewModel.audioData.observeAsState(AudioSample.NONE)
    val audioMagnitudes by viewModel.audioDataState.collectAsState()

    val mediaControllerAdapter = viewModel.mediaControllerAdapter


    VisualizerContent(audioMagnitudes = audioMagnitudes,
        isPlaying = viewModel.isPlaying.state,
        playToolbar = {
            PlayToolbar(mediaControllerAdapter = mediaControllerAdapter,
                        isPlayingState = viewModel.isPlaying.state,
                        scope = scope,
                        navController = navController)
        }
    )

//    val list1: ArrayList<Float> = arrayListOf()
//   audioMagnitudes.forEachIndexed {
//       indx, v ->
//       val height by animateFloatAsState(targetValue = v,
//       animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing))
//       list1.add(height)
//    }




}

@Preview(showBackground = true)
@Composable
private fun VisualizerContent(audioMagnitudes : FloatArray = FloatArray(5),
                            isPlaying : StateFlow<Boolean> = MutableStateFlow(true),
                            topBar:  @Composable () -> Unit = {},
                            playToolbar:  @Composable () -> Unit = {}) {
    Scaffold(topBar = topBar,
              bottomBar = playToolbar) {
        Surface(
            Modifier
                .fillMaxSize()
                .padding(it)) {
            val spaceBetweenBars = 5f
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
 //            BarEqualizer(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(10.dp),
//                bars = audioMagnitudes,
//                spaceBetweenBars = spaceBetweenBars
//            )

                //BarEqualizer(bars = audioMagnitudes)
                FireworkWrapper(frequencyPhases = audioMagnitudes.asList(), insetPx = 50f, isPlayingState = isPlaying)
                //    AnimatedEqualizer(modifier = Modifier.fillMaxSize())
                //  LineEqualizerWithStateListCanvasOnly(frequencyPhases = frequencyPhases.asList())
            }

        }
    }
}




