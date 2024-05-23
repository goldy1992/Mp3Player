package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOn
import androidx.compose.material.icons.filled.RepeatOneOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.RepeatMode

@Composable
fun RepeatButton(repeatModeProvider : () -> RepeatMode = {  RepeatMode.OFF },
                 onClick : (currentRepeatMode : RepeatMode) -> Unit = {}) {

    val currentRepeatMode = repeatModeProvider()
    when (repeatModeProvider()) {
        RepeatMode.ONE -> RepeatOneButton(onClick = { onClick(currentRepeatMode) })
        RepeatMode.ALL -> RepeatAllButton(onClick = { onClick(currentRepeatMode) })
        else -> RepeatOffButton(onClick = { onClick(currentRepeatMode) })
    }
}

@Composable
fun RepeatOneButton(onClick : () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Filled.RepeatOneOn, contentDescription = stringResource(id = R.string.repeat_one),
            tint = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun RepeatAllButton(onClick : () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Filled.RepeatOn, contentDescription = stringResource(id = R.string.repeat_all),
            tint = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun RepeatOffButton(onClick : () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(Icons.Filled.Repeat, contentDescription = stringResource(id = R.string.repeat_none),
            tint = MaterialTheme.colorScheme.primary)
    }
}
