package com.github.goldy1992.mp3player.client.ui.flows.mediabrowser

import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.AsyncMediaBrowserListener
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ActivityRetainedScoped
class OnChildrenChangedFlow
    @Inject
    constructor(
        private val asyncMediaBrowserListener: AsyncMediaBrowserListener) {

    val flow : Flow<OnChildrenChangedEventHolder> = callbackFlow<OnChildrenChangedEventHolder> {
        val messageListener = object : MediaBrowser.Listener {
            override fun onChildrenChanged(
                browser: MediaBrowser,
                parentId: String,
                itemCount: Int,
                params: MediaLibraryService.LibraryParams?
            ) {
                val x = OnChildrenChangedEventHolder(browser, parentId, itemCount, params)
                trySend(x)
            }
        }
        asyncMediaBrowserListener.listeners.add(messageListener)
        awaitClose {
            asyncMediaBrowserListener.listeners.remove(messageListener) }
    }
}