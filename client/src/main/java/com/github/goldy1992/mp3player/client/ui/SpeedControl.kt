package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.goldy1992.mp3player.client.MediaControllerAdapter

@Composable
fun SpeedController(mediaController : MediaControllerAdapter) {

    var sliderPosition by remember { mutableStateOf(mediaController.playbackSpeed.value) }

    Row {
        Slider(
            modifier = Modifier.weight(4f),
            value = sliderPosition!!,
            valueRange = 0.5f..1.5f,
            onValueChange = { sliderPosition = it },
            onValueChangeFinished = { mediaController.changePlaybackSpeed(sliderPosition!!)}
        )
        Text(text = "${String.format("%.2f", sliderPosition)}x",
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically))

        IconButton(modifier = Modifier.weight(1f),
            onClick = { mediaController.changePlaybackSpeed(1f)
                sliderPosition = 1f}
        ) {
            Icon(Icons.Filled.Refresh, contentDescription = "Reset to 1x Speed")
        }
    }

}