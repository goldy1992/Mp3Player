package com.github.goldy1992.mp3player.client.ui.flows.player

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ActivityRetainedScoped
class CurrentMediaItemFlow

@Inject
constructor(mediaControllerFuture: ListenableFuture<MediaController>,
    metadataFlow: MetadataFlow)

    : LogTagger, PlayerFlow<MediaItem>(mediaControllerFuture) {

    private val flow : Flow<MediaItem> = metadataFlow.flow().map {
        val mediaItem : MediaItem? = mediaControllerFuture.await().currentMediaItem
        if (mediaItem == null) {
            Log.w(logTag(), "Current MediaItem is null")
            MediaItem.EMPTY
        } else {
            mediaItem
        }
    }

    override fun flow(): Flow<MediaItem> {
        return flow
    }

    override fun logTag(): String {
        return "currentMediaItemFLow"
    }
}