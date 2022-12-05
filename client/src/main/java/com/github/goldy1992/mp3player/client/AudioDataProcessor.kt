package com.github.goldy1992.mp3player.client

import android.util.Log
import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBand
import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBandFive
import com.github.goldy1992.mp3player.client.utils.calculateFrequencyBandsAverages
import com.github.goldy1992.mp3player.client.utils.createFftTransformedAudioSample
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


    suspend fun processAudioData(audioSample: AudioSample) : FloatArray {
        return withContext(dispatcher) {
            // Log.i(logTag(), "processing audio sample")
            if (audioSample.waveformData.isEmpty()) {
                Log.w(logTag(), "Received empty audio sample")
                floatArrayOf()
            }
            else {
                val processedAudioSample = createFftTransformedAudioSample(audioSample)
                // process audio data on the background thread
                val magnitudesFloatArray: FloatArray =
                calculateFrequencyBandsAverages(values = processedAudioSample.magnitude, frequencyBand = FrequencyBandFive())
                magnitudesFloatArray
            }

        }

    }

    /**
     * @return the name of the log tag given to the class
     */
    override fun logTag(): String {
        return "AUDIO_DATA_PROCESSOR"
    }
}

