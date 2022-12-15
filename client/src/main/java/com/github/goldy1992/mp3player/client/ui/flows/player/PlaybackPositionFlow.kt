package com.github.goldy1992.mp3player.client.ui.flows.player

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.TimerUtils
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@ActivityRetainedScoped
class PlaybackPositionFlow

@Inject
constructor(mediaControllerFuture: ListenableFuture<MediaController>)
    : LogTagger, PlayerFlow<PlaybackPositionEvent>(mediaControllerFuture) {

    private val playbackPositionFlow : Flow<PlaybackPositionEvent> = callbackFlow {
        val controller = mediaControllerFuture.await()
        val messageListener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                Log.i(logTag(), "onIsPlayingChanged: $isPlaying")
                trySend(PlaybackPositionEvent(isPlaying, controller.currentPosition, TimerUtils.getSystemTime()))
            }

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                val isPlaying = controller.isPlaying
                trySend(PlaybackPositionEvent(isPlaying, controller.currentPosition, TimerUtils.getSystemTime()))
            }
        }
        controller.addListener(messageListener)
        awaitClose {
            controller.removeListener(messageListener)
        }
    }

    override fun flow(): Flow<PlaybackPositionEvent> {
        return playbackPositionFlow
    }

    override fun logTag(): String {
        return "PlaybackPositionFlow"
    }

}