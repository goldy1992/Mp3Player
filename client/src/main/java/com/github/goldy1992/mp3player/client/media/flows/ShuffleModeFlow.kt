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

class ShuffleModeFlow
private constructor(
    @ActivityCoroutineScope scope: CoroutineScope,
    private val controllerLf: ListenableFuture<Player>,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    onCollect : suspend (Boolean) -> Unit
) : FlowBase<Boolean>(scope, onCollect) {

    companion object {
        fun create(
            @ActivityCoroutineScope scope : CoroutineScope,
            controllerFuture : ListenableFuture<Player>,
            @MainDispatcher mainDispatcher : CoroutineDispatcher,
            onCollect : (Boolean) -> Unit) : ShuffleModeFlow {
            val shuffleModeFlow = ShuffleModeFlow(scope, controllerFuture, mainDispatcher, onCollect)
            shuffleModeFlow.initFlow(shuffleModeFlow.getFlow())
            return shuffleModeFlow
        }
    }

    override fun getFlow(): Flow<Boolean> = callbackFlow {
        val controller = controllerLf.await()
        val messageListener = object : Player.Listener {
            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                trySend(shuffleModeEnabled)
            }
        }
        controller.addListener(messageListener)

        awaitClose {
            scope.launch(mainDispatcher) {
                controller.removeListener(messageListener)
            }
        }
    }

}