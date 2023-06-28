package com.github.goldy1992.mp3player.client.media.flows

import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnSearchResultsChangedEventHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private const val LOG_TAG = "OnSearchResultFlow"


class OnSearchResultsChangedFlow

private constructor(scope: CoroutineScope,
                    private val addListener: (MediaBrowser.Listener) -> Boolean,
                    private val removeListener: (MediaBrowser.Listener) -> Boolean,
                    onCollect : suspend (OnSearchResultsChangedEventHolder) -> Unit)
    : FlowBase<OnSearchResultsChangedEventHolder>(scope, onCollect) {

    companion object {
        fun create(
            scope: CoroutineScope,
            addListener: (MediaBrowser.Listener) -> Boolean,
            removeListener: (MediaBrowser.Listener) -> Boolean,
            onCollect: suspend (OnSearchResultsChangedEventHolder) -> Unit
        ): OnSearchResultsChangedFlow {
            val onSearchResultsChangedFlow =
                OnSearchResultsChangedFlow(scope, addListener, removeListener, onCollect)
            onSearchResultsChangedFlow.initFlow(onSearchResultsChangedFlow.getFlow())
            return onSearchResultsChangedFlow
        }
    }


    override fun getFlow(): Flow<OnSearchResultsChangedEventHolder> = callbackFlow {
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

    override fun logTag(): String {
        return "OnSearchResultsFlow"
    }
}