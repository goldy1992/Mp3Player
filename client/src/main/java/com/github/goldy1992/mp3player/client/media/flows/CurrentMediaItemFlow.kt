package com.github.goldy1992.mp3player.client.media.flows;

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.commons.ActivityCoroutineScope
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CurrentMediaItemFlow
    private constructor(
        @ActivityCoroutineScope scope: CoroutineScope,
        private val controllerLf: ListenableFuture<Player>,
        @MainDispatcher private val mainDispatcher : CoroutineDispatcher,
        onCollect: (MediaItem) -> Unit
    ) : FlowBase<MediaItem>(scope, onCollect) {

    companion object {
        const val LOG_TAG = "CurrentMediaItemFlow"
        fun create(
            @ActivityCoroutineScope scope: CoroutineScope,
            controllerLf: ListenableFuture<Player>,
            @MainDispatcher mainDispatcher : CoroutineDispatcher,
            onCollect: (MediaItem) -> Unit
        ): CurrentMediaItemFlow {
            val currentMediaItemFlow = CurrentMediaItemFlow(scope, controllerLf, mainDispatcher, onCollect)
            currentMediaItemFlow.initFlow(currentMediaItemFlow.getFlow())
            return currentMediaItemFlow
        }
    }

    override fun getFlow(): Flow<MediaItem> = callbackFlow {
        Log.v(LOG_TAG, "currentMediaItemFlow map invoked")
        val controller: Player = controllerLf.await()
        var mediaItem: MediaItem?

        runBlocking(mainDispatcher) {
            mediaItem = controller.currentMediaItem
        }
        trySend(mediaItem)
        val messageListener = object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                Log.v(LOG_TAG, "onMediaMetadataChanged() invoked, title: ${mediaMetadata.title}")
                val currentMediaItem = controller.currentMediaItem
                val metadata = currentMediaItem?.mediaMetadata
                if (metadata != null && metadata.isBrowsable == false && metadata.isPlayable == true) {
                    val toSend = MediaItem.Builder()
                        .setMediaId(controller.currentMediaItem?.mediaId ?: Constants.UNKNOWN)
                        .setMediaMetadata(mediaMetadata)
                        .build()
                    Log.v(
                        LOG_TAG,
                        "onMediaMetadataChanged() try to Send item ${mediaMetadata.title}"
                    )

                    trySend(toSend)
                } else {
                    Log.v(
                        LOG_TAG,
                        "onMediaMetadataChanged() invalid mediaItem ${if (mediaItem == null ) "null" else "with id " + mediaItem!!.mediaId}"
                    )
                }
            }


        }
        controller.addListener(messageListener)
        awaitClose {
            scope.launch(mainDispatcher) {
                controller.removeListener(messageListener)
            }
        }
    }
    .filterNotNull()


}