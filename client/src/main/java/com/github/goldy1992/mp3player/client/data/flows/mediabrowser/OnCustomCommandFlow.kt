package com.github.goldy1992.mp3player.client.data.flows.mediabrowser

import android.os.Bundle
import androidx.media3.session.*
import com.github.goldy1992.mp3player.client.AsyncMediaBrowserListener
import com.github.goldy1992.mp3player.client.data.eventholders.OnChildrenChangedEventHolder
import com.github.goldy1992.mp3player.client.data.eventholders.SessionCommandEventHolder
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@ActivityRetainedScoped
class OnCustomCommandFlow
    @Inject
    constructor(
        private val asyncMediaBrowserListener: AsyncMediaBrowserListener,
        private val scope : CoroutineScope) {

    val flow : Flow<SessionCommandEventHolder> = callbackFlow<SessionCommandEventHolder> {
        val messageListener = object : MediaBrowser.Listener {
            override fun onCustomCommand(
                controller: MediaController,
                command: SessionCommand,
                args: Bundle
            ): ListenableFuture<SessionResult> {
                trySend(SessionCommandEventHolder(controller, command, args))
                return Futures.immediateFuture(SessionResult(SessionResult.RESULT_SUCCESS))
            }
        }
        asyncMediaBrowserListener.listeners.add(messageListener)
        awaitClose {
            asyncMediaBrowserListener.listeners.remove(messageListener) }
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )
}