package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaBrowser
import com.github.goldy1992.mp3player.client.ui.states.eventholders.SessionCommandEventHolder
import com.github.goldy1992.mp3player.commons.ActivityCoroutineScope
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MetadataFlow

internal constructor(
    @ActivityCoroutineScope scope : CoroutineScope,
    private val controllerFuture : ListenableFuture<MediaBrowser>,
    @MainDispatcher private val mainDispatcher : CoroutineDispatcher,
    onCollect : (MediaMetadata) -> Unit
) : FlowBase<MediaMetadata>(scope, onCollect) {

    companion object {
        fun create(
            @ActivityCoroutineScope scope : CoroutineScope,
            controllerFuture : ListenableFuture<MediaBrowser>,
            @MainDispatcher mainDispatcher : CoroutineDispatcher,
        onCollect : (MediaMetadata) -> Unit) : MetadataFlow {
            val metadataFlow = MetadataFlow(scope, controllerFuture, mainDispatcher, onCollect)
            metadataFlow.initFlow(metadataFlow.getFlow())
            return metadataFlow
        }
    }

    
    override fun getFlow(): Flow<MediaMetadata> = callbackFlow {
        Log.v(logTag(), "metadataFlow invoked, awaiting MediaController")
        val controller = controllerFuture.await()
        Log.v(logTag(), "metadataFlow got MediaController")
        var currentMediaMetadata: MediaMetadata
        withContext(mainDispatcher) {
            currentMediaMetadata = controller.mediaMetadata
        }
        trySend(currentMediaMetadata)
        val messageListener = object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                Log.v(
                    logTag(),
                    "onMediaMetadataChanged() invoked with MediaMetaData: $mediaMetadata"
                )
                trySend(mediaMetadata)
            }
        }
        controller.addListener(messageListener)
        awaitClose {
            scope.launch(mainDispatcher) {
                controller.removeListener(messageListener)
            }
        }
    }

    override fun logTag(): String {
        return "MetadataFlow"
    }

}