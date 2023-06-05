package com.github.goldy1992.mp3player.client.utils

import android.annotation.TargetApi
import android.os.Build
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
        return command.customExtras.getSerializable(
            Constants.AUDIO_DATA,
            AudioSample::class.java) as AudioSample
    }

    @Suppress("DEPRECATION")
    private fun getAudioSampleBelowApi33(eventHolder : SessionCommandEventHolder) : AudioSample {
        val command = eventHolder.command
        return command.customExtras.getSerializable(Constants.AUDIO_DATA) as AudioSample
    }
}