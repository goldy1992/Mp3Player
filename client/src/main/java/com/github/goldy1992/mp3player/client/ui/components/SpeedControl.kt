package com.github.goldy1992.mp3player.client.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SpeedController(modifier : Modifier = Modifier,
                    playbackSpeedProvider: () -> Float,
                    changePlaybackSpeed : (speed : Float) -> Unit) {

    val sliderPosition = playbackSpeedProvider()

    var uiSliderPosition : Float by remember { mutableStateOf(sliderPosition)  }
    var isTouchTracking by remember { mutableStateOf(false)   }
    var touchTrackingPosition : Float by remember { mutableStateOf(0f) }

    Column(modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally) {
        Slider(
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
        Row(horizontalArrangement = Arrangement.Center) {
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
                Icon(Icons.Filled.Refresh, contentDescription = "Reset to 1x Speed",
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