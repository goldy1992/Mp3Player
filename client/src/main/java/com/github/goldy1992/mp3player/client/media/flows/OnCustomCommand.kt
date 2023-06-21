package com.github.goldy1992.mp3player.client.media.flows

import android.os.Bundle
import android.util.Log
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private const val LOG_TAG = "OnCustomCommandFlow"

fun onCustomCommandFlow(
    addListener : (MediaBrowser.Listener) -> Boolean,
    removeListener : (MediaBrowser.Listener) -> Boolean
): Flow<SessionCommandEventHolder> = callbackFlow {
    val messageListener = object : MediaBrowser.Listener {
        override fun onCustomCommand(
            controller: MediaController,
            command: SessionCommand,
            args: Bundle
        ): ListenableFuture<SessionResult> {
            Log.v(LOG_TAG, "onCustomCommand() invoked")
            trySend(SessionCommandEventHolder(command, args))
            return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
        }
    }
    addListener(messageListener)
    awaitClose {
        removeListener(messageListener)
    }
}