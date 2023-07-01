package com.github.goldy1992.mp3player.client.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.R

@Preview
@Composable
fun SpeedController(modifier : Modifier = Modifier,
                    playbackSpeedProvider: () -> Float = {1f},
                    changePlaybackSpeed : (speed : Float) -> Unit = {_->}){

    val sliderPosition = playbackSpeedProvider()

    var uiSliderPosition : Float by remember { mutableStateOf(sliderPosition)  }
    var isTouchTracking by remember { mutableStateOf(false)   }
    var touchTrackingPosition : Float by remember { mutableStateOf(0f) }

    Column(modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally) {
        Slider(
            modifier = Modifier.weight(1f),
            value = if (isTouchTracking) touchTrackingPosition else uiSliderPosition,
            valueRange = 0.5f..1.5f,
            onValueChange = {
                isTouchTracking = true
                touchTrackingPosition = it

            },
            onValueChangeFinished = {
                isTouchTracking = false
                uiSliderPosition = touchTrackingPosition
                changePlaybackSpeed(uiSliderPosition)
            }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.weight(1f)) {
            Text(
                text = "${String.format("%.2f", sliderPosition)}x",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )

            IconButton(
                onClick = {
                    changePlaybackSpeed(1f)
                    uiSliderPosition = 1f
                }
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = stringResource(id = R.string.reset_speed),
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }

}

@Preview
@Composable
fun SpeedControllerPreview() {
   // SpeedController()
}