package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaBrowser
import com.github.goldy1992.mp3player.commons.ActivityCoroutineScope
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val LOG_TAG = "MetadataFlow"

fun metadataFlow(controllerLf: ListenableFuture<MediaBrowser>,
                @MainDispatcher mainDispatcher : CoroutineDispatcher,
                 @ActivityCoroutineScope scope : CoroutineScope
) : Flow<MediaMetadata> = callbackFlow {
    Log.v(LOG_TAG, "metadataFlow invoked, awaiting MediaController")
    val controller = controllerLf.await()
    Log.v(LOG_TAG, "metadataFlow got MediaController")
    var currentMediaMetadata: MediaMetadata
    withContext(mainDispatcher) {
        currentMediaMetadata = controller.mediaMetadata
    }
    trySend(currentMediaMetadata)
    val messageListener = object : Player.Listener {
        override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
            Log.v(
                LOG_TAG,
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