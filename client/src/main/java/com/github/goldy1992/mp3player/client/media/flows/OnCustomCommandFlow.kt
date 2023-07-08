package com.github.goldy1992.mp3player.client.media.flows

import android.os.Bundle
import android.util.Log
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.SessionCommandEventHolder
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class OnCustomCommandFlow

private constructor(scope: CoroutineScope,
            private val addListener: (MediaBrowser.Listener) -> Boolean,
            private val removeListener: (MediaBrowser.Listener) -> Boolean,
            onCollect : suspend (SessionCommandEventHolder) -> Unit)
    : FlowBase<SessionCommandEventHolder>(scope, onCollect) {

    companion object {
        fun create(
            scope: CoroutineScope,
            addListener: (MediaBrowser.Listener) -> Boolean,
            removeListener: (MediaBrowser.Listener) -> Boolean,
            onCollect : suspend (SessionCommandEventHolder) -> Unit) : OnCustomCommandFlow {
            val onCustomCommandFlow = OnCustomCommandFlow(scope, addListener, removeListener, onCollect)
            onCustomCommandFlow.initFlow(onCustomCommandFlow.getFlow())
            return onCustomCommandFlow
        }
    }

    override fun getFlow() : Flow<SessionCommandEventHolder> = callbackFlow {
        val messageListener = object : MediaBrowser.Listener {
            override fun onCustomCommand(
                controller: MediaController,
                command: SessionCommand,
                args: Bundle
            ): ListenableFuture<SessionResult> {
                Log.v(logTag(), "onCustomCommand() invoked")
                trySend(SessionCommandEventHolder(command, args))
                return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
            }
        }
        addListener(messageListener)
        awaitClose {
            removeListener(messageListener)
        }
    }

    override fun logTag(): String {
        return "OnCustomFlow"
    }

}