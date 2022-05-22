package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.MediaControllerAdapter

@Composable
fun SpeedController(mediaController : MediaControllerAdapter? = null,
                    modifier: Modifier = Modifier) {

    var sliderPosition by remember { mutableStateOf(if (mediaController != null) mediaController?.playbackSpeed?.value else 0.5f) }

    Column(modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally) {
        Slider(
//            modifier = Modifier.weight(4f),
            value = sliderPosition!!,
            valueRange = 0.5f..1.5f,
            onValueChange = { sliderPosition = it },
            onValueChangeFinished = { mediaController?.changePlaybackSpeed(sliderPosition!!)}
        )
        Row(horizontalArrangement = Arrangement.Center) {
            Text(
                text = "${String.format("%.2f", sliderPosition)}x",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    //             .weight(1f)
                    .align(Alignment.CenterVertically)
            )

            IconButton(//modifier = Modifier.weight(1f),
                onClick = {
                    mediaController?.changePlaybackSpeed(1f)
                    sliderPosition = 1f
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
    SpeedController()
}