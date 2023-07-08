package com.github.goldy1992.mp3player.client.media.flows

import androidx.concurrent.futures.await
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

class RepeatModeFlow
    private constructor(
        @ActivityCoroutineScope scope: CoroutineScope,
        private val controllerLf: ListenableFuture<Player>,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
        onCollect : suspend (@Player.RepeatMode Int) -> Unit
    ) : FlowBase<@Player.RepeatMode Int>(scope, onCollect) {

    companion object {
        fun create(
            @ActivityCoroutineScope scope : CoroutineScope,
            controllerFuture : ListenableFuture<Player>,
            @MainDispatcher mainDispatcher : CoroutineDispatcher,
            onCollect : (@Player.RepeatMode Int) -> Unit) : RepeatModeFlow {
            val repeatModeFlow = RepeatModeFlow(scope, controllerFuture, mainDispatcher, onCollect)
            repeatModeFlow.initFlow(repeatModeFlow.getFlow())
            return repeatModeFlow
        }
    }

    override fun getFlow(): Flow<Int> = callbackFlow {
        val controller = controllerLf.await()
        val messageListener = object : Player.Listener {
            override fun onRepeatModeChanged(@Player.RepeatMode repeatMode: Int) {
                trySend(repeatMode)
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
        return "RepeatModeFlow"
    }
}