package com.github.goldy1992.mp3player.client.ui.flows.player

import android.annotation.TargetApi
import android.os.Build.VERSION_CODES.TIRAMISU
import android.util.Log
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.client.ui.flows.mediabrowser.OnCustomCommandFlow
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.Constants.AUDIO_DATA
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ActivityRetainedScoped
class AudioDataFlow

@Inject
constructor(customCommandFlow: OnCustomCommandFlow) : LogTagger {

    private val audioDataFlow : Flow<AudioSample> = customCommandFlow.flow
    .filter {
        Log.i(logTag(), "audioDataFlow filter")
        AUDIO_DATA == it.command.customAction
    }.map {
        getAudioSample(it)
    }

    override fun logTag(): String {
        return "AudioDataFlow"
    }

    fun flow(): Flow<AudioSample> {
        return audioDataFlow
    }


    private fun getAudioSample(eventHolder: SessionCommandEventHolder) : AudioSample {
        return if (android.os.Build.VERSION.SDK_INT >= TIRAMISU) {
            getAudioSampleApi33AndAbove(eventHolder)
        } else {
            getAudioSampleBelowApi33(eventHolder)
        }
    }
    @TargetApi(TIRAMISU)
    private fun getAudioSampleApi33AndAbove(eventHolder : SessionCommandEventHolder) : AudioSample {
        val command = eventHolder.command
        return command.customExtras.getSerializable(AUDIO_DATA,AudioSample::class.java) as AudioSample
    }

    @Suppress("DEPRECATION")
    private fun getAudioSampleBelowApi33(eventHolder : SessionCommandEventHolder) : AudioSample {
        val command = eventHolder.command
        return command.customExtras.getSerializable(AUDIO_DATA) as AudioSample
    }


}