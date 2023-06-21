package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.client.utils.AudioDataUtils
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

private const val LOG_TAG = "AudioDataFlow"

fun audioDataFlow(onCustomCommandFlow : Flow<SessionCommandEventHolder>): Flow<AudioSample> = onCustomCommandFlow
    .filter {
        Log.v(LOG_TAG, "audioDataFlow() filter invoked")
        Constants.AUDIO_DATA == it.command.customAction
    }.map {
        Log.v(LOG_TAG, "audioDataFlow() map invoked")
        AudioDataUtils.getAudioSample(it)
    }