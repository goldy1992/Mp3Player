package com.github.goldy1992.mp3player.client

import android.util.Log
import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBand
import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBandTwentyFour
import com.github.goldy1992.mp3player.client.utils.calculateFrequencyBandsAverages
import com.github.goldy1992.mp3player.client.utils.createFftTransformedAudioSample
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.IoDispatcher
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class AudioDataProcessor

    @Inject
    constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher) {

    companion object {
        const val LOG_TAG = "AudioDataProcessor"
    }
    suspend fun processAudioData(audioSample: AudioSample,
                                frequencyBand: FrequencyBand = FrequencyBandTwentyFour()) : FloatArray {
        Log.v(LOG_TAG, "processAudioData() invoked.")
        return withContext(dispatcher) {
            if (audioSample.waveformData.isEmpty()) {
                Log.w(LOG_TAG, "processAudioData() received empty audio sample")
                floatArrayOf()
            }
            else {
                val processedAudioSample = createFftTransformedAudioSample(audioSample)
                // process audio data on the background thread
                val magnitudesFloatArray: FloatArray =
                calculateFrequencyBandsAverages(values = processedAudioSample.magnitude, frequencyBand = frequencyBand)
                magnitudesFloatArray
            }

        }

    }

    /**
     * @return the name of the log tag given to the class
     */
}

