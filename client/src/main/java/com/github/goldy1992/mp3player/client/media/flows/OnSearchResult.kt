package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private const val LOG_TAG = "OnSearchResultFlow"

fun onSearchResultsChangedFlow(
    addListener : (MediaBrowser.Listener) -> Boolean,
    removeListener : (MediaBrowser.Listener) -> Boolean
) : Flow<OnSearchResultsChangedEventHolder> = callbackFlow {
    val messageListener = object : MediaBrowser.Listener {
        override fun onSearchResultChanged(
            browser: MediaBrowser,
            query: String,
            itemCount: Int,
            params: MediaLibraryService.LibraryParams?
        ) {
            trySend(OnSearchResultsChangedEventHolder(query, itemCount, params))
        }
    }
   addListener(messageListener)
    awaitClose {
        removeListener(messageListener)
    }
}