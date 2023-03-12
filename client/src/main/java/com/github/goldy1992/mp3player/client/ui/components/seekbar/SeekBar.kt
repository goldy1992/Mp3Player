package com.github.goldy1992.mp3player.client.ui.components.seekbar

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.RepeatMode
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.utils.SeekbarUtils.calculateAnimationTime
import com.github.goldy1992.mp3player.client.utils.SeekbarUtils.calculateCurrentPosition
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.MetadataUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val logTag = "seekbar"

@Composable
fun SeekBar(isPlayingProvider: () -> Boolean = {true},
            metadataProvider: () -> MediaMetadata = {MediaMetadata.EMPTY},
            playbackSpeedProvider : () ->  Float = {1.0f},
            playbackPositionProvider: () -> PlaybackPositionEvent = {PlaybackPositionEvent.DEFAULT},
            seekTo: (value: Long) -> Unit = {},
            repeatModeProvider: @Player.RepeatMode ()-> Int = { Player.REPEAT_MODE_OFF },
            scope: CoroutineScope = rememberCoroutineScope()) {

    //Log.i(logTag, "seek bar created")
    val isPlaying = isPlayingProvider()
    val metadata = metadataProvider()
    val playbackSpeed = playbackSpeedProvider()
    val playbackPositionEvent = playbackPositionProvider()
    val duration = MetadataUtils.getDuration(metadata).toFloat()
    val currentPosition = calculateCurrentPosition(playbackPositionEvent).toFloat()
    Log.i(logTag, "current playback position: $currentPosition")
    val animationTimeInMs = calculateAnimationTime(currentPosition, duration, playbackSpeed)
    val durationDescription = stringResource(id = R.string.duration)
    val currentPositionDescription = stringResource(id = R.string.current_position)

    SeekBarUi(
        currentPosition = currentPosition,
        duration = duration,
        isPlaying = isPlaying,
        animationTimeInMs = animationTimeInMs,
        durationDescription = durationDescription,
        currentPositionDescription = currentPositionDescription,
        scope = scope,
        seekTo = seekTo
    )
}

@Composable
private fun SeekBarUi(currentPosition : Float,
                      duration : Float,
                      isPlaying : Boolean,
                      animationTimeInMs : Int,
                      durationDescription : String,
                      currentPositionDescription : String,
                      scope: CoroutineScope,
                      seekTo : (value : Long) -> Unit
                    ) {
    val seekBarAnimation = remember(animationTimeInMs) { mutableStateOf(Animatable(currentPosition)) }
    //  Log.i(logTag, "Anim1Value: ${anim1.value}")

    if (isPlaying) {
        //   Log.i(logTag, "playback state playing")
        LaunchedEffect(seekBarAnimation) {
            Log.i(logTag, "animating to duration: $duration, currentPos: $currentPosition, animationTimeMs: $animationTimeInMs")
            seekBarAnimation.value.animateTo(duration,
                animationSpec = FloatTweenSpec(animationTimeInMs, 0, LinearEasing))
            //      Log.i(logTag, "animating")
        }
    }
    val isTouchTracking = remember { mutableStateOf(false)   }
    val touchTrackingPosition = remember { mutableStateOf(0f) }

    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        Text(text = formatTime(duration.toLong()),
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)
                .semantics {
                    contentDescription = durationDescription
                },
            textAlign = TextAlign.Center)
        Slider(
            modifier = Modifier.weight(5f),
            value = if (isTouchTracking.value) touchTrackingPosition.value else seekBarAnimation.value.value ,
            valueRange = 0f..duration,
            onValueChange = {
                isTouchTracking.value = true
                touchTrackingPosition.value = it
            },
            onValueChangeFinished = {
                isTouchTracking.value = false
                seekBarAnimation.value = Animatable(touchTrackingPosition.value)
                scope.launch { seekTo(touchTrackingPosition.value.toLong()) }
            })
        Text(text = formatTime(if (isTouchTracking.value) touchTrackingPosition.value.toLong() else seekBarAnimation.value.value.toLong()),
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)
                .semantics { contentDescription = currentPositionDescription },
            textAlign = TextAlign.Center)
    }
}