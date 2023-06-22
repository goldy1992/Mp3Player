package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaBrowser
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private const val LOG_TAG = "CurrentMediaItemFlow"

fun currentMediaItemFlow(
    mediaMetadataStateFlow : Flow<MediaMetadata>,
    controllerLf: ListenableFuture<MediaBrowser>,
    @MainDispatcher mainDispatcher : CoroutineDispatcher): Flow<MediaItem> = mediaMetadataStateFlow.map {
    Log.v(LOG_TAG, "currentMediaItemFlow map invoked")
    val mediaBrowser: MediaBrowser = controllerLf.await()
    var mediaItem: MediaItem?

    runBlocking(mainDispatcher) {
        mediaItem = mediaBrowser.currentMediaItem
    }
    if (mediaItem == null) {
        Log.w(LOG_TAG, "currentMediaItemFlow currentMediaItem is NULL")
    }
    Log.d(LOG_TAG, "current media item: ${mediaItem?.mediaId}")
    mediaItem
}
    .filterNotNull()