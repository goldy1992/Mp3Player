package com.github.goldy1992.mp3player.client.data.flows.mediabrowser

import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.AsyncMediaBrowserListener
import com.github.goldy1992.mp3player.client.data.eventholders.OnChildrenChangedEventHolder
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@ActivityRetainedScoped


class OnChildrenChangedFlow
    @Inject
    constructor(
        private val asyncMediaBrowserListener: AsyncMediaBrowserListener,
        private val scope : CoroutineScope) {

    val flow : Flow<OnChildrenChangedEventHolder> = callbackFlow {
        val messageListener = object : MediaBrowser.Listener {
            override fun onChildrenChanged(
                browser: MediaBrowser,
                parentId: String,
                itemCount: Int,
                params: MediaLibraryService.LibraryParams?
            ) {
                trySend(OnChildrenChangedEventHolder(browser, parentId, itemCount, params))
            }
        }
        asyncMediaBrowserListener.listeners.add(messageListener)
        awaitClose { asyncMediaBrowserListener.listeners.remove(messageListener) }
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )
}