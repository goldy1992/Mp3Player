package com.github.goldy1992.mp3player.client.ui

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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.AsyncPlayerListener
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun SpeedController(mediaController : MediaControllerAdapter? = null,
                    asyncPlayerListener: AsyncPlayerListener,
                    modifier : Modifier = Modifier,
                    scope: CoroutineScope = rememberCoroutineScope()) {

    var sliderPosition = 0f // by remember { mutableStateOf(if (mediaController != null) mediaController.playbackSpeed.value else 0.5f) }

    Column(modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally) {
        Slider(
            value = sliderPosition!!,
            valueRange = 0.5f..1.5f,
            onValueChange = { sliderPosition = it },
            onValueChangeFinished = { scope.launch { mediaController?.changePlaybackSpeed(sliderPosition!!) }
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

                    scope.launch { mediaController?.changePlaybackSpeed(1f) }
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
   // SpeedController()
}