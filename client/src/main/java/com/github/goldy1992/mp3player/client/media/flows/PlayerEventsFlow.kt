package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.commons.LoggingUtils
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch


class PlayerEventsFlow

private constructor(scope: CoroutineScope,
                     private val controllerFuture : ListenableFuture<Player>,
                     @MainDispatcher private val mainDispatcher : CoroutineDispatcher,
                     onCollect : suspend (Player.Events) -> Unit)
    : FlowBase<Player.Events>(scope, onCollect) {

    companion object {
        const val LOG_TAG = "PlayerEventsFlow"
        fun create(
            scope: CoroutineScope,
            controllerFuture : ListenableFuture<Player>,
            @MainDispatcher mainDispatcher : CoroutineDispatcher,
            onCollect : suspend (Player.Events) -> Unit) : PlayerEventsFlow {
            val playerEventsFlow = PlayerEventsFlow(scope, controllerFuture, mainDispatcher,  onCollect)
            playerEventsFlow.initFlow(playerEventsFlow.getFlow())
            return playerEventsFlow
        }
    }

    override fun getFlow() : Flow<Player.Events> = callbackFlow {
        val browser = controllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                Log.i(LOG_TAG, "newEvent(s): ${LoggingUtils.getPlayerEventsLogMessage(events)}")
                super.onEvents(player, events)
                trySend(events)
            }
        }
        browser.addListener(messageListener)
        awaitClose {
            scope.launch(mainDispatcher) {
                browser.removeListener(messageListener)
            }
        }
    }


}