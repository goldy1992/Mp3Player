package com.github.goldy1992.mp3player.client.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.goldy1992.mp3player.client.ui.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.commons.AudioSample

val SUB_BASS = IntRange(20, 60)
val BASS = IntRange(61, 250)
val LOW_MIDRANGE = IntRange(251, 500)
val MIDRANGE = IntRange(501, 2000)
val UPPER_MIDRANGE = IntRange(2001, 4000)

val frequencyBands = listOf(SUB_BASS, BASS, LOW_MIDRANGE, MIDRANGE, UPPER_MIDRANGE)


@Composable
fun VisualizerScreen(viewModel: VisualizerViewModel = viewModel()) {

    val audioSample by viewModel.audioData.observeAsState(AudioSample.NONE)
    Surface(Modifier.fillMaxSize()) {
        val magnitudeFreqs = calculateFrequencyBands(audioSample.magnitude)
        val phaseFreqs = calculateFrequencyBands(audioSample.phase)
        Column {
            Text(text = "Frequency Range: 0 - ${audioSample.sampleHz}")
            Text(text = "Phase data")
            Text(text = phaseFreqs.joinToString(", "))
            Text(text = "Magnitude data")
            Text(text = magnitudeFreqs.joinToString(", "))
            PlayPauseButton(mediaController = viewModel.mediaControllerAdapter)
        }

    }
}
private fun calculateFrequencyBands(magnitudes: Array<Double>) : List<Double> {
    val toReturn = ArrayList<Double>()
    val numOfFreq = magnitudes.size

    for (frequencyRange in frequencyBands) {
        if (frequencyRange.last <= numOfFreq) {
            var sum = 0.0
            frequencyRange.forEach { range -> sum += magnitudes[range] }
            val average = sum / (frequencyRange.last - frequencyRange.first)
            toReturn.add(average)
        }
    }

    return toReturn
}



