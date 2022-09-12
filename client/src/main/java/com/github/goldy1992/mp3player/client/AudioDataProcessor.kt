package com.github.goldy1992.mp3player.client

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.views.audiobands.FrequencyBand
import com.github.goldy1992.mp3player.client.views.audiobands.FrequencyBandFive
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class AudioDataProcessor

    @Inject
    constructor() : LogTagger {

    open val audioStream: MutableLiveData<AudioSample> = MutableLiveData(AudioSample.NONE)
    open val audioMagnitudes: MutableLiveData<FloatArray> = MutableLiveData(floatArrayOf())
    open val frequencyPhases : MutableLiveData<FloatArray> = MutableLiveData(floatArrayOf())

    fun processAudioData(audioSample: AudioSample) {
        CoroutineScope(Dispatchers.Default).launch {
            // process audio data on the background thread
            //val magnitudes: FloatArray = calculateFrequencyBands(audioSample.magnitude)
         //   val frequencyPhasesFloatArray : FloatArray = calculateFrequencyBandsAverages(audioSample.phase)
//            val frequencyPhasesFloatArray : FloatArray = calculateReadValues(audioSample.magnitude, 50)
            val magnitudesFloatArray : FloatArray = calculateReadValues(audioSample.magnitude, 10)
            audioMagnitudes.postValue(magnitudesFloatArray)
           // frequencyPhases.postValue(frequencyPhasesFloatArray)
        }
    }

    var maxFrequency : Float = 0f

    private suspend fun calculateFrequencyBandsAverages(values: Array<Double>, frequencyBand: FrequencyBand = FrequencyBandFive()): FloatArray {
        val frequencyBands = frequencyBand.frequencyBands()
        val toReturn = FloatArray(frequencyBands.size)
        val numOfFreq = values.size
        val maxVal = values.maxOrNull() ?: 0
        if (maxVal.toFloat() > maxFrequency) {
            maxFrequency = maxVal.toFloat()
            Log.i("adProcessor", "max magnitude ${values.maxOrNull() ?: 0}")
        }

        frequencyBands.forEachIndexed { idx, frequencyRange ->

            if (frequencyRange.last <= numOfFreq) {
                var sum = 0.0
                frequencyRange.forEach { range -> sum += values[range] }
                val average = sum / (frequencyRange.last - frequencyRange.first)
                toReturn[idx] = average.toFloat()
            }
        }

        return toReturn
    }

    private suspend fun covertFrequencyPhases(originalFrequencyPhases : Array<Double>) : FloatArray {
        val toReturn = FloatArray(originalFrequencyPhases.size)
        originalFrequencyPhases.forEachIndexed {idx, value -> toReturn[idx] = value.toFloat()}

        return toReturn
    }

    private suspend fun calculateReadValues(values: Array<Double>, readCount : Int) : FloatArray {
            val valuesCount = values.size
            if (readCount > valuesCount) {
                Log.w(logTag(), "readCount ($readCount) >= values array size ($valuesCount), returning empty list")
                return floatArrayOf()
            }

            val toReturn = FloatArray(readCount)
            var count = 0
            val stepsOf = valuesCount / readCount
            for (i in 0 until readCount) {
                toReturn[i] = values[count].toFloat()
                count += stepsOf
            }
            return toReturn
    }

    /**
     * @return the name of the log tag given to the class
     */
    override fun logTag(): String {
        return "AUDIO_DATA_PROCESSOR"
    }
}

