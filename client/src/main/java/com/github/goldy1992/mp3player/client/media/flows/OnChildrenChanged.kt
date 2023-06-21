package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.ui.states.eventholders.OnChildrenChangedEventHolder
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private const val LOG_TAG = "OnChildrenChangedFlow"

fun onChildrenChangedFlow(addListener : (MediaBrowser.Listener) -> Boolean,
                          removeListener : (MediaBrowser.Listener) -> Boolean) : Flow<OnChildrenChangedEventHolder> = callbackFlow {
    val messageListener = object : MediaBrowser.Listener {
        override fun onChildrenChanged(
            browser: MediaBrowser,
            parentId: String,
            itemCount: Int,
            params: MediaLibraryService.LibraryParams?
        ) {
            Log.v(LOG_TAG, "onChildrenChangedFlow() invoked - parent: $parentId, itemCount: $itemCount, params $params")
            val x = OnChildrenChangedEventHolder(parentId, itemCount, params)
            trySend(x)
        }
    }
    addListener(messageListener)
    awaitClose {
        removeListener(messageListener)
    }
}