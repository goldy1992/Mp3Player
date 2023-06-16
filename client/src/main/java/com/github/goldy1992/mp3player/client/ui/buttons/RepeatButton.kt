package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOn
import androidx.compose.material.icons.filled.RepeatOneOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.media3.common.Player
import androidx.media3.common.Player.RepeatMode
import com.github.goldy1992.mp3player.client.R

@Composable
fun RepeatButton(repeatModeProvider : () -> @RepeatMode Int = {  Player.REPEAT_MODE_OFF },
                 onClick : (currentRepeatMode : @RepeatMode Int) -> Unit = {}) {

    val currentRepeatMode = repeatModeProvider()
    when (repeatModeProvider()) {
        Player.REPEAT_MODE_ONE -> RepeatOneButton(onClick = { onClick(currentRepeatMode) })
        Player.REPEAT_MODE_ALL -> RepeatAllButton(onClick = { onClick(currentRepeatMode) })
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
