package com.github.goldy1992.mp3player.client.media.flows

import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.client.utils.AudioDataUtils
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

/**
 * Flow of [AudioSample] data, which is wrapped in a [SessionCommandEventHolder].
 * The flow receives a stream of [SessionCommandEventHolder] objects. [SessionCommandEventHolder]
 * are identified as [AudioSample]s by filtering on [SessionCommandEventHolder.command]s with the
 * value [Constants.AUDIO_DATA]. The [AudioSample] can then be received as a [Serializable] extra
 * under the key, also named [Constants.AUDIO_DATA].
 */
class AudioDataFlow private constructor(
    scope : CoroutineScope,
    private val onCustomCommandFlow : Flow<SessionCommandEventHolder>,
    onCollect : (AudioSample) -> Unit
) : FlowBase<AudioSample>(scope, onCollect) {


    companion object {
        /**
         * Creates an instance of [AudioDataFlow].
         * @param scope The [CoroutineScope] to run in.
         * @param onCustomCommandFlow The [OnCustomCommandFlow] to subscribe to.
         * @param onCollect The function to invoke when the flow receives data.
         */
        fun create(scope : CoroutineScope,
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
            Constants.AUDIO_DATA == it.command.customAction
        }.map {
            AudioDataUtils.getAudioSample(it)
        }

}