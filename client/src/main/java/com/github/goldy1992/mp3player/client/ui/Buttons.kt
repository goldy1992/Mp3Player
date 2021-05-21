package com.github.goldy1992.mp3player.client.ui

import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.github.goldy1992.mp3player.client.MediaControllerAdapter


@Composable
fun PlayPauseButton(mediaController: MediaControllerAdapter) {
    val isPlaying by mediaController.isPlaying.observeAsState()
    if (isPlaying!!) {
        PauseButton(mediaController = mediaController)
    } else {
        PlayButton(mediaController = mediaController)
    }
}



@Composable
fun PlayButton(mediaController : MediaControllerAdapter) {
    Button(onClick = { mediaController.play()}) {
        Icon(
            Icons.Filled.PlayArrow,
            contentDescription = "Play"
        )
    }
}

@Composable
fun SkipToPreviousButton(mediaController: MediaControllerAdapter) {
    Button(onClick = {mediaController.skipToPrevious()}) {
        Icon(
            Icons.Filled.SkipPrevious,
            "Skip to Previous"
        )
    }
}

@Composable
fun SkipToNextButton(mediaController: MediaControllerAdapter) {
    Button(onClick = {mediaController.skipToNext()}) {
        Icon(
            Icons.Filled.SkipNext,
            "Skip to Previous"
        )
    }
}

@Composable
fun PauseButton(mediaController : MediaControllerAdapter) {
    Button(onClick = { mediaController.pause()}) {
        Icon(
            Icons.Filled.Pause,
            contentDescription = "Pause"
        )
    }
}