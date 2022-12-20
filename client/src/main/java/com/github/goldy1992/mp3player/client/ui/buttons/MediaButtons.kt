package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun SkipToPreviousButton(onClick : () -> Unit = {} ) {
    IconButton(onClick = { onClick() }){
        Icon(
            Icons.Filled.SkipPrevious,
            "Skip to Previous",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SkipToNextButton(onClick: () -> Unit = {}) {
    IconButton(onClick = { onClick() }) {
        Icon(
            Icons.Filled.SkipNext,
            "Skip to Previous",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}