package com.github.goldy1992.mp3player.client

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.views.audiobands.FrequencyBand
import com.github.goldy1992.mp3player.client.views.audiobands.FrequencyBandFive
import com.github.goldy1992.mp3player.client.views.audiobands.FrequencyBandThirtyOne
import com.github.goldy1992.mp3player.client.views.audiobands.FrequencyBandTwentyFour
import com.github.goldy1992.mp3player.commons.AudioSample
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class AudioDataProcessor

    @Inject
    constructor() {

    open val audioStream: MutableLiveData<AudioSample> = MutableLiveData(AudioSample.NONE)
    open val audioMagnitudes: MutableLiveData<FloatArray> = MutableLiveData(floatArrayOf())

    fun processAudioData(audioSample: AudioSample) {
        CoroutineScope(Dispatchers.Default).launch {
            // process audio data on the background thread
            val magnitudes: FloatArray = calculateFrequencyBands(audioSample.magnitude)
            audioMagnitudes.postValue(magnitudes)
        }
    }


    var maxFrequency : Float = 0f

    private suspend fun calculateFrequencyBands(magnitudes: Array<Double>, frequencyBand: FrequencyBand = FrequencyBandFive()): FloatArray {
        val frequencyBands = frequencyBand.frequencyBands()
        val toReturn = FloatArray(frequencyBands.size)
        val numOfFreq = magnitudes.size
        val maxVal = magnitudes.maxOrNull() ?: 0
        if (maxVal.toFloat() > maxFrequency) {
            maxFrequency = maxVal.toFloat()
            Log.i("adProcessor", "max magnitude ${magnitudes.maxOrNull() ?: 0}")
        }

        frequencyBands.forEachIndexed { idx, frequencyRange ->

            if (frequencyRange.last <= numOfFreq) {
                var sum = 0.0
                frequencyRange.forEach { range -> sum += magnitudes[range] }
                val average = sum / (frequencyRange.last - frequencyRange.first)
                toReturn[idx] = average.toFloat()
            }

        }

        return toReturn
    }
}

