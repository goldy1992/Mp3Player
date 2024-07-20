package com.github.goldy1992.mp3player.client.ui.components.seekbar

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.utils.SeekbarUtils.calculateAnimationTime
import com.github.goldy1992.mp3player.client.utils.SeekbarUtils.calculateCurrentPosition
import com.github.goldy1992.mp3player.client.utils.SeekbarUtils.validPlaybackPosition
import com.github.goldy1992.mp3player.client.utils.SeekbarUtils.validSong
import com.github.goldy1992.mp3player.client.utils.TimeUtils.formatTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val LOG_TAG = "SeekBar"

@Composable
fun SeekBar(isPlayingProvider: () -> Boolean = {true},
            currentSongProvider: () -> Song = { Song.DEFAULT},
            playbackSpeedProvider : () ->  Float = {1.0f},
            playbackPositionProvider: () -> PlaybackPositionEvent = {PlaybackPositionEvent.DEFAULT},
            seekTo: (value: Long) -> Unit = {},
            scope: CoroutineScope = rememberCoroutineScope()) {

    Log.v(LOG_TAG, "SeekBar() recomposed")
    val isPlaying = isPlayingProvider()
    val song = currentSongProvider()
    val playbackSpeed = playbackSpeedProvider()
    val playbackPositionEvent = playbackPositionProvider()
    val duration = song.duration.toFloat()
    val currentPosition = calculateCurrentPosition(playbackPositionEvent).toFloat()
    Log.v(LOG_TAG, "SeekBar() current playback position: $currentPosition")
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
        song = song,
        seekTo = seekTo
    )
}




@Composable
fun PlaybackPositionAnimation(
    isPlaying: Boolean = true,
    song: Song = Song.DEFAULT,
    playbackSpeed :  Float = 1.0f,
    playbackPositionEvent :  PlaybackPositionEvent = PlaybackPositionEvent.DEFAULT,
    content: @Composable (Float) -> Unit
) {
    if (validPlaybackPosition(song, playbackPositionEvent)) {
        Log.v(LOG_TAG, "SeekBar() recomposed")
        val duration = song.duration.toFloat()
        val currentPosition = calculateCurrentPosition(playbackPositionEvent).toFloat()
        Log.v(LOG_TAG, "SeekBar() current playback position: $currentPosition")
        val animationTimeInMs = calculateAnimationTime(currentPosition, duration, playbackSpeed)

        val currentProgress = currentPosition / duration
        val seekBarAnimation =
            remember(animationTimeInMs) { mutableStateOf(Animatable(currentProgress)) }
        val canPlay = isPlaying && validSong(song)
        Log.i(LOG_TAG, "canPlay: $canPlay")
        if (canPlay) {
            LaunchedEffect(seekBarAnimation) {
                Log.i(
                    LOG_TAG,
                    "animating to duration: $duration, currentPos: $currentPosition, animationTimeMs: $animationTimeInMs"
                )
                seekBarAnimation.value.animateTo(
                    1.0f,
                    animationSpec = FloatTweenSpec(animationTimeInMs, 0, LinearEasing)
                )
            }
        }
        content(seekBarAnimation.value.value)
    }
}


@Composable
private fun SeekBarUi(currentPosition : Float,
                      duration : Float,
                      isPlaying : Boolean,
                      animationTimeInMs : Int,
                      durationDescription : String,
                      currentPositionDescription : String,
                      scope: CoroutineScope,
                      song: Song,
                      seekTo : (value : Long) -> Unit
                    ) {
    val seekBarAnimation = remember(animationTimeInMs) { mutableStateOf(Animatable(currentPosition)) }

    val canPlay = isPlaying && validSong(song)

    if (canPlay) {
        LaunchedEffect(seekBarAnimation) {
            Log.i(LOG_TAG, "animating to duration: $duration, currentPos: $currentPosition, animationTimeMs: $animationTimeInMs")
            seekBarAnimation.value.animateTo(duration,
                animationSpec = FloatTweenSpec(animationTimeInMs, 0, LinearEasing))
        }
    }
    val isTouchTracking = remember { mutableStateOf(false)   }
    val touchTrackingPosition = remember { mutableFloatStateOf(0f) }

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
            value = if (isTouchTracking.value) touchTrackingPosition.floatValue else seekBarAnimation.value.value ,
            valueRange = 0f..duration,
            onValueChange = {
                isTouchTracking.value = true
                touchTrackingPosition.floatValue = it
            },
            onValueChangeFinished = {
                isTouchTracking.value = false
                seekBarAnimation.value = Animatable(touchTrackingPosition.floatValue)
                scope.launch { seekTo(touchTrackingPosition.floatValue.toLong()) }
            })
        Text(text = formatTime(if (isTouchTracking.value) touchTrackingPosition.floatValue.toLong() else seekBarAnimation.value.value.toLong()),
            modifier = Modifier
                .weight(2f)
                .align(Alignment.CenterVertically)
                .semantics { contentDescription = currentPositionDescription },
            textAlign = TextAlign.Center)
    }
}