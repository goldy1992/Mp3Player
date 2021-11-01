package com.github.goldy1992.mp3player.client.ui

import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.MetadataUtils

const val logTag = "seekbar"

@Composable
fun SeekBar(mediaController : MediaControllerAdapter) {

    //Log.i(logTag, "seek bar created")
    val metadata by mediaController.metadata.observeAsState()
    val playbackState by mediaController.playbackState.observeAsState()

    val duration = MetadataUtils.getDuration(metadata).toFloat()
    val playbackSpeed = playbackState?.playbackSpeed ?: 1f
    val currentPosition = playbackState?.position?.toFloat() ?: 0f

    val durationAtSpeed = duration / playbackSpeed
    val animationTimeInMs = (durationAtSpeed * (1 - (currentPosition / duration))).toInt()
    val durationDescription = stringResource(id = R.string.duration)
    val currentPositionDescription = stringResource(id = R.string.current_position)

    val anim1 = remember(currentPosition) { mutableStateOf(Animatable(currentPosition)) }
  //  Log.i(logTag, "Anim1Value: ${anim1.value}")

    if (playbackState?.state == PlaybackStateCompat.STATE_PLAYING) {
     //   Log.i(logTag, "playback state playing")
        LaunchedEffect(anim1) {
            anim1.value.animateTo(duration,
            animationSpec = FloatTweenSpec(animationTimeInMs.toInt(), 0, LinearEasing))
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
            value = if (isTouchTracking.value) touchTrackingPosition.value else anim1.value.value ,
            valueRange = 0f..duration,
            onValueChange = {
                isTouchTracking.value = true
                touchTrackingPosition.value = it
            },
            onValueChangeFinished = {
                isTouchTracking.value = false
                anim1.value = Animatable(touchTrackingPosition.value)
                mediaController.seekTo(touchTrackingPosition.value.toLong())
            })
        Text(text = formatTime(if (isTouchTracking.value) touchTrackingPosition.value.toLong() else anim1.value.value.toLong()),
                modifier = Modifier
                    .weight(2f)
                    .align(Alignment.CenterVertically)
                    .semantics { contentDescription = currentPositionDescription },
        textAlign = TextAlign.Center)
    }

}