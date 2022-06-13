package com.github.goldy1992.mp3player.client.ui.screens


import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.goldy1992.mp3player.client.ui.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.client.ui.components.Equalizer
import com.github.goldy1992.mp3player.commons.AudioSample


@Composable
fun VisualizerScreen(viewModel: VisualizerViewModel = viewModel()) {

    val audioSample by viewModel.audioData.observeAsState(AudioSample.NONE)

    val list1: ArrayList<Float> = arrayListOf()
    audioSample.frequencyMap.forEachIndexed {
       indx, v ->
       val height by animateFloatAsState(targetValue = v,
       animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing))
       list1.add(height)
    }


    Surface(Modifier.fillMaxSize()) {
        val spaceBetweenBars = 10.dp
        Column {
            Text(text = "Frequency Range: 0 - ${audioSample.sampleHz}")
            PlayPauseButton(mediaController = viewModel.mediaControllerAdapter)
            Equalizer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                bars = list1,
                spaceBetweenBars = spaceBetweenBars
            )

        }

    }

}




