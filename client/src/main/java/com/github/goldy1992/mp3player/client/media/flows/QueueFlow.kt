package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnQueueChangedEventHolder
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
        onCollect : suspend (OnQueueChangedEventHolder) -> Unit)
        
     : FlowBase<OnQueueChangedEventHolder>(scope, onCollect) {
    
    companion object {
        const val LOG_TAG = "QueueFlow"
        fun create(
            controllerLf: ListenableFuture<Player>,
            @MainDispatcher mainDispatcher: CoroutineDispatcher,
            @ActivityCoroutineScope scope: CoroutineScope,
            onCollect : suspend (OnQueueChangedEventHolder) -> Unit
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
    
    override fun getFlow(): Flow<OnQueueChangedEventHolder> = callbackFlow {
        Log.v(LOG_TAG, "QueueFlow callbackFlow invoked, awaiting MediaController")
        val controller = controllerFuture.await()
        Log.v(LOG_TAG, "QueueFlow callbackFlow finished awaiting MediaController")
        var queue: OnQueueChangedEventHolder
        withContext(mainDispatcher) {
            queue = QueueUtils.getQueue(controller)
        }
        Log.d(LOG_TAG, "QueueFlow finished initialising with queue ")
        trySend(queue)
        val messageListener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                Log.d(LOG_TAG, "QueueFlow.onEvents() invoked ${LoggingUtils.getPlayerEventsLogMessage(events)}")
                if (events.containsAny(*QueueFlow.events)) {
                    Log.d(LOG_TAG, "QueueFlow.onEvents() sending queue events ${QueueFlow.events}")
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

}