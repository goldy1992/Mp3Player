package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.Player
import androidx.media3.session.MediaBrowser
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

private const val LOG_TAG = "QueueFlow"

private val events : @Player.Event IntArray = intArrayOf(
    Player.EVENT_MEDIA_ITEM_TRANSITION,
    Player.EVENT_TIMELINE_CHANGED,
    Player.EVENT_TRACKS_CHANGED,
    Player.EVENT_MEDIA_METADATA_CHANGED
)

fun queueFlow(
    controllerLf : ListenableFuture<MediaBrowser>,
    @MainDispatcher mainDispatcher : CoroutineDispatcher,
    @ActivityCoroutineScope scope : CoroutineScope

) : Flow<QueueState> = callbackFlow {
    Log.v(LOG_TAG, "QueueFlow callbackFlow invoked, awaiting MediaController")
    val controller = controllerLf.await()
    Log.v(LOG_TAG, "QueueFlow callbackFlow finished awaiting MediaController")
    var queue: QueueState = QueueState.EMPTY
    withContext(mainDispatcher) {
        queue = QueueUtils.getQueue(controller)
    }
    Log.d(LOG_TAG, "QueueFlow finished initialising with queue ")
    trySend(queue)
    val messageListener = object : Player.Listener {
        override fun onEvents(player: Player, event: Player.Events) {
            Log.d(LOG_TAG, "QueueFlow.onEvents() invoked")
            val e = LoggingUtils.getPlayerEventsLogMessage(event)
            if (event.containsAny( *events )) {
                Log.d(LOG_TAG, "QueueFlow.onEvents() sending queue envents $events")
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