package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import androidx.media3.session.MediaBrowser
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.data.repositories.media.eventholders.OnChildrenChangedEventHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

private const val LOG_TAG = "OnChildrenChangedFlow"

class OnChildrenChangedFlow

    private constructor(
        scope: CoroutineScope,
        private val addListener : (MediaBrowser.Listener) -> Boolean,
        private val removeListener : (MediaBrowser.Listener) -> Boolean,
        onCollect : suspend (OnChildrenChangedEventHolder) -> Unit
    ) : FlowBase<OnChildrenChangedEventHolder>(scope, onCollect) {

    companion object {
        fun create(
            scope: CoroutineScope,
            addListener : (MediaBrowser.Listener) -> Boolean,
            removeListener : (MediaBrowser.Listener) -> Boolean,
            onCollect : suspend (OnChildrenChangedEventHolder) -> Unit
        ) : OnChildrenChangedFlow {
            val onChildrenChangedEventFlow = OnChildrenChangedFlow(scope, addListener, removeListener, onCollect)
            onChildrenChangedEventFlow.initFlow(onChildrenChangedEventFlow.getFlow())
            return onChildrenChangedEventFlow
        }
    }
    override fun getFlow(): Flow<OnChildrenChangedEventHolder> = callbackFlow {
        val messageListener = object : MediaBrowser.Listener {
            override fun onChildrenChanged(
                browser: MediaBrowser,
                parentId: String,
                itemCount: Int,
                params: MediaLibraryService.LibraryParams?
            ) {
                Log.v(
                    LOG_TAG,
                    "onChildrenChangedFlow() invoked - parent: $parentId, itemCount: $itemCount, params $params"
                )
                val x = OnChildrenChangedEventHolder(parentId, itemCount, params)
                trySend(x)
            }
        }
        addListener(messageListener)
        awaitClose {
            removeListener(messageListener)
        }
    }

    override fun logTag(): String {
        return "OnChildrenChangedFlow"
    }
}