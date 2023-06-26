package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaBrowser
import com.github.goldy1992.mp3player.commons.ActivityCoroutineScope
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CurrentPlaylistMetadataFlow

private constructor(
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
        onCollect : (MediaMetadata) -> Unit) : CurrentPlaylistMetadataFlow {
            val toCreate = CurrentPlaylistMetadataFlow(scope, controllerFuture, mainDispatcher, onCollect)
            toCreate.initFlow(toCreate.getFlow())
            return toCreate
        }
    }

    override fun getFlow(): Flow<MediaMetadata> = callbackFlow {
        val controller =  controllerFuture.await()
        runBlocking(mainDispatcher) {
            trySend(controller.playlistMetadata ?: MediaMetadata.EMPTY)
        }
        val messageListener = object : Player.Listener {
            override fun onPlaylistMetadataChanged(mediaMetadata: MediaMetadata) {
                Log.v(
                    logTag(),
                    "onPlaylistMetadataChanged() invoked with playlistId: ${
                        mediaMetadata.extras?.getString(Constants.PLAYLIST_ID) ?: Constants.UNKNOWN
                    }"
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
        return "CurrentPlaylistMetadataFlow"
    }

}