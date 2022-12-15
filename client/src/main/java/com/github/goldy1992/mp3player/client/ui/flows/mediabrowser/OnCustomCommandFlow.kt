package com.github.goldy1992.mp3player.client.ui.flows.mediabrowser

import android.os.Bundle
import android.util.Log
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaController
import androidx.media3.session.SessionCommand
import androidx.media3.session.SessionResult
import com.github.goldy1992.mp3player.client.AsyncMediaBrowserListener
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ActivityRetainedScoped
class OnCustomCommandFlow
    @Inject
    constructor(
        private val asyncMediaBrowserListener: AsyncMediaBrowserListener) : LogTagger {

    val flow : Flow<SessionCommandEventHolder> = callbackFlow<SessionCommandEventHolder> {
        val messageListener = object : MediaBrowser.Listener {
            override fun onCustomCommand(
                controller: MediaController,
                command: SessionCommand,
                args: Bundle
            ): ListenableFuture<SessionResult> {
                Log.i(logTag(), "onCustomCommand")
                trySend(SessionCommandEventHolder(controller, command, args))
                return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
            }
        }
        asyncMediaBrowserListener.listeners.add(messageListener)
        awaitClose {
            asyncMediaBrowserListener.listeners.remove(messageListener) }
    }

    override fun logTag(): String {
        return "OnCustomCommandFlow"
    }
}