package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOn
import androidx.compose.material.icons.filled.RepeatOneOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.flows.player.RepeatModeFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RepeatButton(mediaController : MediaControllerAdapter,
                repeatModeFlow: RepeatModeFlow,
                scope: CoroutineScope = rememberCoroutineScope()) {
    val repeatMode by repeatModeFlow.state.collectAsState()
    when (repeatMode) {
        Player.REPEAT_MODE_ONE -> RepeatOneButton(mediaController = mediaController, scope = scope)
        Player.REPEAT_MODE_ALL -> RepeatAllButton(mediaController = mediaController, scope = scope)
        else -> RepeatNoneButton(mediaController = mediaController, scope = scope)
    }
}

@Composable
fun RepeatOneButton(mediaController : MediaControllerAdapter,
                    scope : CoroutineScope = rememberCoroutineScope()) {
    IconButton(onClick = { scope.launch { mediaController.setRepeatMode(Player.REPEAT_MODE_ALL)} }) {
        Icon(Icons.Filled.RepeatOneOn, contentDescription = stringResource(id = R.string.repeat_one),
            tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun RepeatAllButton(mediaController : MediaControllerAdapter,
                    scope: CoroutineScope = rememberCoroutineScope()) {
    IconButton(onClick = {scope.launch { mediaController.setRepeatMode(Player.REPEAT_MODE_OFF)}}) {
        Icon(Icons.Filled.RepeatOn, contentDescription = stringResource(id = R.string.repeat_all),
            tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun RepeatNoneButton(mediaController : MediaControllerAdapter,
                    scope: CoroutineScope = rememberCoroutineScope()) {
   IconButton(onClick = { scope.launch { mediaController.setRepeatMode(Player.REPEAT_MODE_ONE)}}) {
       Icon(Icons.Filled.Repeat, contentDescription = stringResource(id = R.string.repeat_none),
           tint = MaterialTheme.colorScheme.onSurfaceVariant)
   }
}

