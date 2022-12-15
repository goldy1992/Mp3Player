package com.github.goldy1992.mp3player.client.ui.flows.player

import androidx.concurrent.futures.await
import androidx.media3.common.PlaybackParameters
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
class PlaybackParametersFlow
@Inject
constructor(mediaControllerFuture: ListenableFuture<MediaController>)
    : LogTagger, PlayerFlow<PlaybackParameters>(mediaControllerFuture) {

    private val playbackParametersFlow : Flow<PlaybackParameters> = callbackFlow {
        val controller = mediaControllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                trySend(playbackParameters)
            }
        }
        controller.addListener(messageListener)
        awaitClose { controller.removeListener(messageListener) }
    }

    override fun logTag(): String {
        return "PlaybackParametersFlow"
    }

    override fun flow(): Flow<PlaybackParameters> {
        return playbackParametersFlow
    }
}