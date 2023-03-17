package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SkipToPreviousButton(
                        modifier: Modifier = Modifier,
                        onClick : () -> Unit = {} ) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() }){
        Icon(
            imageVector = Icons.Filled.SkipPrevious,
            contentDescription = "Skip to Previous",
            tint = MaterialTheme.colorScheme.primary,
        //    modifier = modifier,
        )
    }
}

@Composable
fun SkipToNextButton(modifier: Modifier = Modifier,
                    onClick: () -> Unit = {}) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.SkipNext,
            contentDescription = "Skip to Previous",
            tint = MaterialTheme.colorScheme.primary,
    //        modifier = modifier,
        )
    }
}