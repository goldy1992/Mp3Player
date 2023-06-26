package com.github.goldy1992.mp3player.client.media.flows;

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaBrowser
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class CurrentMediaItemFlow
    private constructor(
        scope: CoroutineScope,
        private val mediaMetadataStateFlow : Flow<MediaMetadata>,
        private val controllerLf: ListenableFuture<MediaBrowser>,
        @MainDispatcher private val mainDispatcher : CoroutineDispatcher,
        onCollect: (MediaItem) -> Unit
    ) : FlowBase<MediaItem>(scope, onCollect) {

    companion object {
        fun create(
            scope: CoroutineScope,
            mediaMetadataStateFlow : Flow<MediaMetadata>,
            controllerLf: ListenableFuture<MediaBrowser>,
            @MainDispatcher mainDispatcher : CoroutineDispatcher,
            onCollect: (MediaItem) -> Unit
        ): CurrentMediaItemFlow {
            val currentMediaItemFlow = CurrentMediaItemFlow(scope, mediaMetadataStateFlow, controllerLf, mainDispatcher, onCollect)
            currentMediaItemFlow.initFlow(currentMediaItemFlow.getFlow())
            return currentMediaItemFlow
        }
    }


    override fun getFlow(): Flow<MediaItem> = mediaMetadataStateFlow.map {
        Log.v(logTag(), "currentMediaItemFlow map invoked")
        val mediaBrowser: MediaBrowser = controllerLf.await()
        var mediaItem: MediaItem?

        runBlocking(mainDispatcher) {
            mediaItem = mediaBrowser.currentMediaItem
        }
        if (mediaItem == null) {
            Log.w(logTag(), "currentMediaItemFlow currentMediaItem is NULL")
        }
        Log.d(logTag(), "current media item: ${mediaItem?.mediaId}")
        mediaItem
    }
        .filterNotNull()

    override fun logTag(): String {
        return "CurrentMediaItemFlow"
    }

}