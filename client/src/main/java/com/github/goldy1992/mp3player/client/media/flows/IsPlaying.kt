package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.Player
import androidx.media3.session.MediaBrowser
import com.github.goldy1992.mp3player.commons.ActivityCoroutineScope
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val LOG_TAG = "isPlayingFlow"

fun isPlayingCallbackFlow(controllerFuture : ListenableFuture<MediaBrowser>,
    @ActivityCoroutineScope scope : CoroutineScope,
    @MainDispatcher mainDispatcher : CoroutineDispatcher) : Flow<Boolean> = callbackFlow {
    Log.v(LOG_TAG, "isPlayingFlow invoked, awaiting MediaController")
    val controller = controllerFuture.await()
    Log.v(LOG_TAG, "isPlayingFlow got MediaController")
    var isPlaying: Boolean
    withContext(mainDispatcher) {
        isPlaying = controller.isPlaying
    }
    trySend(isPlaying)
    val messageListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            Log.v(LOG_TAG, "onIsPlayingChanged() invoked, isPlaying: $isPlaying")
            trySend(isPlaying)
        }
    }
    controller.addListener(messageListener)
    awaitClose {
        Log.d(LOG_TAG, "isPlayingCallbackFlow() awaitClose invoked")
        scope.launch(mainDispatcher) {
            controller.removeListener(messageListener)
        }
    }
}