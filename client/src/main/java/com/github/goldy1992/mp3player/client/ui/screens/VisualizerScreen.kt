package com.github.goldy1992.mp3player.client.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.goldy1992.mp3player.client.ui.PlayToolbar
import com.github.goldy1992.mp3player.client.ui.components.equalizer.fireworks.FireworkWrapper

@Composable
fun VisualizerScreen(viewModel: VisualizerViewModel = viewModel()) {

    //val audioSample by viewModel.audioData.observeAsState(AudioSample.NONE)
    val audioMagnitudes by viewModel.audioMagnitudes.observeAsState(floatArrayOf())
    val frequencyPhases by viewModel.frequencyPhases.observeAsState(floatArrayOf())
    val isPlaying by viewModel.mediaControllerAdapter.isPlaying.observeAsState(false)

//    val list1: ArrayList<Float> = arrayListOf()
//   audioMagnitudes.forEachIndexed {
//       indx, v ->
//       val height by animateFloatAsState(targetValue = v,
//       animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing))
//       list1.add(height)
//    }


    Surface(Modifier.fillMaxSize()) {
        val spaceBetweenBars = 5f
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          //  Text(text = "Frequency Range: 0 - ${audioSample.sampleHz}")
            PlayToolbar(mediaController = viewModel.mediaControllerAdapter) {

            }
//            BarEqualizer(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(10.dp),
//                bars = audioMagnitudes,
//                spaceBetweenBars = spaceBetweenBars
//            )

            //BarEqualizer(bars = audioMagnitudes)
            FireworkWrapper(frequencyPhases = audioMagnitudes.asList(), insetPx = 50f, isPlaying = isPlaying)
        //    AnimatedEqualizer(modifier = Modifier.fillMaxSize())
          //  LineEqualizerWithStateListCanvasOnly(frequencyPhases = frequencyPhases.asList())
        }

    }

}




