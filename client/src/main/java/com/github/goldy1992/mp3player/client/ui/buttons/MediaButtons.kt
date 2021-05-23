package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import com.github.goldy1992.mp3player.client.MediaControllerAdapter

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