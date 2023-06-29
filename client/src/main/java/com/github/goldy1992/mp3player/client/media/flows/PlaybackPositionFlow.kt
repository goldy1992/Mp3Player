package com.github.goldy1992.mp3player.client.media.flows

import android.util.Log
import androidx.concurrent.futures.await
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.commons.ActivityCoroutineScope
import com.github.goldy1992.mp3player.commons.LoggingUtils
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.github.goldy1992.mp3player.commons.TimerUtils
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlaybackPositionFlow
    private constructor(
        private val controllerLf: ListenableFuture<Player>,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
        @ActivityCoroutineScope scope: CoroutineScope,
        onCollect : (PlaybackPositionEvent) -> Unit
    )  : FlowBase<PlaybackPositionEvent>(scope, onCollect) {


    companion object {
        fun create(
            @ActivityCoroutineScope scope : CoroutineScope,
            controllerFuture : ListenableFuture<Player>,
            @MainDispatcher mainDispatcher : CoroutineDispatcher,
            onCollect : (PlaybackPositionEvent) -> Unit) : PlaybackPositionFlow {
            val playbackPositionFlow = PlaybackPositionFlow(controllerFuture, mainDispatcher, scope, onCollect)
            playbackPositionFlow.initFlow(playbackPositionFlow.getFlow())
            return playbackPositionFlow
        }
    }

    val playbackPositionEvents: @Player.Event IntArray = intArrayOf(
        Player.EVENT_POSITION_DISCONTINUITY,
        Player.EVENT_IS_PLAYING_CHANGED,
        Player.EVENT_PLAYBACK_PARAMETERS_CHANGED
    )
    
    override fun getFlow(): Flow<PlaybackPositionEvent> = callbackFlow {
            val controller = controllerLf.await()
            withContext(mainDispatcher) {
                trySend(
                    PlaybackPositionEvent(
                        controller.isPlaying,
                        controller.currentPosition,
                        TimerUtils.getSystemTime()
                    )
                )
            }
            val messageListener = object : Player.Listener {
                override fun onEvents(player: Player, events: Player.Events) {
                    Log.v(logTag(), "onEvents() invoked.")
                    if (events.containsAny(*playbackPositionEvents)) {
                        val currentPosition = player.currentPosition
                        val isPlaying = player.isPlaying
                        Log.d(
                            logTag(),
                            "onEvents() playbackPosition changed due to ${
                                LoggingUtils.getPlayerEventsLogMessage(events)
                            } with position $currentPosition, isPlaying: $isPlaying"
                        )
                        trySend(
                            PlaybackPositionEvent(
                                isPlaying,
                                currentPosition,
                                TimerUtils.getSystemTime()
                            )
                        )
                    }
                }

                override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
                    val isPlaying = controller.isPlaying
                    val currentPosition = controller.currentPosition
                    trySend(
                        PlaybackPositionEvent(
                            isPlaying ?: false,
                            currentPosition ?: 0L,
                            TimerUtils.getSystemTime()
                        )
                    )
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
        return "PlaybackPositionFlow"
    }
}

