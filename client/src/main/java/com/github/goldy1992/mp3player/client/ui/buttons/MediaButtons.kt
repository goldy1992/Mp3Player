package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.github.goldy1992.mp3player.client.R

@Composable
fun SkipToPreviousButton(
                        modifier: Modifier = Modifier,
                        onClick : () -> Unit = {} ) {
    IconButton(
        modifier = modifier,
        onClick = { onClick() }){
        Icon(
            imageVector = Icons.Filled.SkipPrevious,
            contentDescription = stringResource(id = R.string.skip_to_previous),
            tint = MaterialTheme.colorScheme.primary,
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
            contentDescription = stringResource(id = R.string.skip_to_next),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}