package com.github.goldy1992.mp3player.client.ui.flows.mediabrowser

import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.AsyncMediaBrowserListener
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@ActivityRetainedScoped
class OnSearchResultsChangedFlow
@Inject
constructor(private val asyncMediaBrowserListener: AsyncMediaBrowserListener,
            private val scope : CoroutineScope) {

    val flow : Flow<OnSearchResultsChangedEventHolder> = callbackFlow {
        val messageListener = object : MediaBrowser.Listener {
            override fun onSearchResultChanged(
                browser: MediaBrowser,
                query: String,
                itemCount: Int,
                params: MediaLibraryService.LibraryParams?
            ) {

                trySend(OnSearchResultsChangedEventHolder(browser, query, itemCount, params))
            }
        }
        asyncMediaBrowserListener.listeners.add(messageListener)
        awaitClose()
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )
}