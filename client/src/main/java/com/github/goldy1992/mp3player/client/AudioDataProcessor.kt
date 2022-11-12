package com.github.goldy1992.mp3player.client

import android.util.Log
import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBand
import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBandFive
import com.github.goldy1992.mp3player.client.ui.screens.createFftSample
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.IoDispatcher
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class AudioDataProcessor

    @Inject
    constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher) : LogTagger {


//    // Backing property to avoid state updates from other classes
//    private val isScreenActiveMutableState = MutableStateFlow(floatArrayOf())
//    // The UI collects from this StateFlow to get its state updates
//    val isScreenActiveState: StateFlow<FloatArray> = audioDataMutableState
//
//
//    val lifeResumedFlow : Flow<Boolean> = callbackFlow {
//        val messagesListener = object : DefaultLifecycleObserver {
//        override fun onResume(owner: LifecycleOwner) {
//           trySend(true)
//        }
//
//        override fun onPause(owner: LifecycleOwner) {
//            trySend(false)
//        }
//    }

    suspend fun processAudioData(audioSample: AudioSample) : FloatArray {
        return withContext(dispatcher) {
         //   Log.i(logTag(), "processing audio sample")
            if (audioSample.waveformData.isEmpty()) {
                floatArrayOf()
            }
            else {
                val processedAudioSample = createFftSample(
                    audioSample.waveformData,
                    audioSample.channelCount,
                    audioSample.sampleHz
                )
                // process audio data on the background thread
                val magnitudesFloatArray: FloatArray =
                calculateReadValues(processedAudioSample.magnitude, 10)

                var str = ""
                for (f in magnitudesFloatArray) {
                    str += "$f, "
                }
            //    Log.i(logTag(), "result array: $str")
                magnitudesFloatArray
            }

        }

        // frequencyPhases.postValue(frequencyPhasesFloatArray)

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

