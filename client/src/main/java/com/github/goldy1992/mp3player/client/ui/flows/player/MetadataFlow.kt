package com.github.goldy1992.mp3player.client.ui.flows.player

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.commons.LogTagger
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

@ActivityRetainedScoped
class MetadataFlow
@Inject
constructor(mediaControllerFuture: ListenableFuture<MediaController>)

    : LogTagger, PlayerFlow<MediaMetadata>(mediaControllerFuture) {

    private val mediaMetadataCallbackFlow : Flow<MediaMetadata> = callbackFlow {
        val controller = mediaControllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                Log.i(logTag(), "onMediaMetadataChanged: $mediaMetadata")
                trySend(mediaMetadata)
            }
        }
        controller.addListener(messageListener)
        awaitClose {

            controller.removeListener(messageListener)
        }
    }

    override fun logTag(): String {
        return "MetadataFlow"
    }

    override fun flow(): Flow<MediaMetadata> {
        return mediaMetadataCallbackFlow
    }

}