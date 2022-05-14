package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.github.goldy1992.mp3player.client.MediaControllerAdapter

@Composable
fun SkipToPreviousButton(mediaController: MediaControllerAdapter) {
    IconButton(onClick = {mediaController.skipToPrevious()}) {
        Icon(
            Icons.Filled.SkipPrevious,
            "Skip to Previous",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SkipToNextButton(mediaController: MediaControllerAdapter) {
    IconButton(onClick = {mediaController.skipToNext()}) {
        Icon(
            Icons.Filled.SkipNext,
            "Skip to Previous",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}