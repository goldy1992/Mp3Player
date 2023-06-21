package com.github.goldy1992.mp3player.client.media.flows

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

private const val LOG_TAG = "RepeatModeFlow"

fun repeatModeFlow(
    controllerLf : ListenableFuture<MediaBrowser>,
    @MainDispatcher mainDispatcher : CoroutineDispatcher,
    @ActivityCoroutineScope scope : CoroutineScope
) : Flow<@Player.RepeatMode Int> = callbackFlow {
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