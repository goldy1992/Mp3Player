package com.github.goldy1992.mp3player.client.media.flows

import androidx.concurrent.futures.await
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.commons.ActivityCoroutineScope
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class PlaybackParametersFlow

    private constructor(@ActivityCoroutineScope scope: CoroutineScope,
                             private val controllerFuture : ListenableFuture<Player>,
                             @MainDispatcher private val mainDispatcher : CoroutineDispatcher,
                             onCollect : suspend (PlaybackParameters) -> Unit
) : FlowBase<PlaybackParameters>(scope, onCollect) {

    companion object {
        fun create(
            @ActivityCoroutineScope scope: CoroutineScope,
           controllerFuture : ListenableFuture<Player>,
            @MainDispatcher mainDispatcher : CoroutineDispatcher,
            onCollect : suspend (PlaybackParameters) -> Unit) : PlaybackParametersFlow {
            val playbackParametersFlow = PlaybackParametersFlow(scope, controllerFuture, mainDispatcher, onCollect)
            playbackParametersFlow.initFlow(playbackParametersFlow.getFlow())
            return playbackParametersFlow
        }
    }

    override fun getFlow(): Flow<PlaybackParameters> = callbackFlow {
        val controller = controllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                trySend(playbackParameters)
            }
        }
        controller.addListener(messageListener)
        awaitClose {
            scope.launch(mainDispatcher) {
                controller.removeListener(messageListener)
            }
        }
    }

    override fun logTag(): String {
        TODO("Not yet implemented")
    }
}
