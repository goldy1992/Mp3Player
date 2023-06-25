package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.client.utils.AudioDataUtils
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

class AudioDataFlow internal constructor(
    scope : CoroutineScope,
    private val onCustomCommandFlow : Flow<SessionCommandEventHolder>,
    onCollect : (AudioSample) -> Unit
) : FlowBase<AudioSample>(scope, onCollect) {


    companion object {
        fun create( scope : CoroutineScope,
                    onCustomCommandFlow : Flow<SessionCommandEventHolder>,
                    onCollect : (AudioSample) -> Unit) : AudioDataFlow {
            val audioDataFlow = AudioDataFlow(scope, onCustomCommandFlow, onCollect)
            audioDataFlow.initFlow(audioDataFlow.getFlow())
            return audioDataFlow
        }
    }


    override fun logTag(): String {
        return "AudioDataFlow"
    }
    
    override fun getFlow(): Flow<AudioSample> = onCustomCommandFlow
        .filter {
            Log.v(
                logTag(),
                "audioDataFlow() filter invoked, filtering: ${Constants.AUDIO_DATA == it.command.customAction}"
            )
            Constants.AUDIO_DATA == it.command.customAction
        }.map {

            val newAudioSample = AudioDataUtils.getAudioSample(it)
            Log.v(logTag(), "audioDataFlow() map invoked: ${newAudioSample.hashCode()}, ${newAudioSample.waveformData.joinToString()}")
            newAudioSample
        }

}