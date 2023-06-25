package com.github.goldy1992.mp3player.client.utils

import android.annotation.TargetApi
import android.os.Build
import android.util.Log
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.Constants

object AudioDataUtils {

    fun getAudioSample(eventHolder: SessionCommandEventHolder) : AudioSample {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getAudioSampleApi33AndAbove(eventHolder)
        } else {
            getAudioSampleBelowApi33(eventHolder)
        }
    }
    @TargetApi(Build.VERSION_CODES.TIRAMISU)
    private fun getAudioSampleApi33AndAbove(eventHolder : SessionCommandEventHolder) : AudioSample {
        val command = eventHolder.command
        val audioSample = command.customExtras.getSerializable(
            Constants.AUDIO_DATA,
            AudioSample::class.java) as AudioSample
        Log.v("AudioDataUtils", "new sample: ${audioSample.waveformData.contentHashCode()}")
        return AudioSample(phase = audioSample.phase.clone(),
            magnitude = audioSample.magnitude.clone(),
        waveformData = audioSample.waveformData.clone(),
        sampleHz = audioSample.sampleHz,
        channelCount = audioSample.channelCount)
    }

    @Suppress("DEPRECATION")
    private fun getAudioSampleBelowApi33(eventHolder : SessionCommandEventHolder) : AudioSample {
        val command = eventHolder.command
        val audioSample =  command.customExtras.getSerializable(Constants.AUDIO_DATA) as AudioSample
        return AudioSample(phase = audioSample.phase.clone(),
            magnitude = audioSample.magnitude.clone(),
            waveformData = audioSample.waveformData.clone(),
            sampleHz = audioSample.sampleHz,
            channelCount = audioSample.channelCount)
    }
}