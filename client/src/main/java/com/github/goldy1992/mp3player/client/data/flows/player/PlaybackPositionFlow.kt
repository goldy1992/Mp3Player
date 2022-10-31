package com.github.goldy1992.mp3player.client.data.flows.player

import android.os.SystemClock
import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.data.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.TimerUtils
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

class PlaybackPositionFlow

@Inject
constructor(mediaControllerFuture: ListenableFuture<MediaController>,
            scope : CoroutineScope
) : LogTagger, PlayerFlow<PlaybackPositionEvent>(mediaControllerFuture, scope) {

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
    }.shareIn(
        scope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )
    override fun flow(): Flow<PlaybackPositionEvent> {
        return playbackPositionFlow
    }

    override fun logTag(): String {
        return "PlaybackPositionFlow"
    }

}