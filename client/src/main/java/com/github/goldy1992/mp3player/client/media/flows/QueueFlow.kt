package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.utils.QueueUtils
import com.github.goldy1992.mp3player.commons.ActivityCoroutineScope
import com.github.goldy1992.mp3player.commons.LoggingUtils
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class QueueFlow
    private constructor(
        @ActivityCoroutineScope scope: CoroutineScope,
        private val controllerFuture : ListenableFuture<Player>,
        @MainDispatcher private val  mainDispatcher : CoroutineDispatcher,
        onCollect : suspend (QueueState) -> Unit)
        
     : FlowBase<QueueState>(scope, onCollect) {
    
    companion object {
        fun create(
            controllerLf: ListenableFuture<Player>,
            @MainDispatcher mainDispatcher: CoroutineDispatcher,
            @ActivityCoroutineScope scope: CoroutineScope,
            onCollect : suspend (QueueState) -> Unit
        ): QueueFlow {
            val queueFlow = QueueFlow(scope, controllerLf, mainDispatcher, onCollect)  
            queueFlow.initFlow(queueFlow.getFlow())
            return queueFlow
        }

        private val events: @Player.Event IntArray = intArrayOf(
            Player.EVENT_MEDIA_ITEM_TRANSITION,
            Player.EVENT_TIMELINE_CHANGED,
            Player.EVENT_TRACKS_CHANGED,
            Player.EVENT_MEDIA_METADATA_CHANGED
        )
    }
    
    override fun getFlow(): Flow<QueueState> = callbackFlow {
        Log.v(logTag(), "QueueFlow callbackFlow invoked, awaiting MediaController")
        val controller = controllerFuture.await()
        Log.v(logTag(), "QueueFlow callbackFlow finished awaiting MediaController")
        var queue: QueueState = QueueState.EMPTY
        withContext(mainDispatcher) {
            queue = QueueUtils.getQueue(controller)
        }
        Log.d(logTag(), "QueueFlow finished initialising with queue ")
        trySend(queue)
        val messageListener = object : Player.Listener {
            override fun onEvents(player: Player, event: Player.Events) {
                Log.d(logTag(), "QueueFlow.onEvents() invoked")
                val e = LoggingUtils.getPlayerEventsLogMessage(event)
                if (event.containsAny(*QueueFlow.events)) {
                    Log.d(logTag(), "QueueFlow.onEvents() sending queue envents ${QueueFlow.events}")
                    trySend(QueueUtils.getQueue(player))
                }
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
        return "QueueFlow"
    }
}