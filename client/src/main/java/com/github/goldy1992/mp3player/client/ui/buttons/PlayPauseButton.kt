package com.github.goldy1992.mp3player.client.ui.buttons

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val LOG_TAG = "PlayPauseButton"
/**
 * This button will display the [PlayButton] if the [MediaControllerAdapter] says there is currently
 * no playback, otherwise it will display the [PauseButton].
 */
@Composable
fun PlayPauseButton(mediaController: MediaControllerAdapter,
                    isPlayingState: StateFlow<Boolean>,
                    scope: CoroutineScope = rememberCoroutineScope()) {
    val isPlayingValue by isPlayingState.collectAsState()
    if (isPlayingValue) {
        PauseButton(mediaController = mediaController, scope)
    } else {
        PlayButton(mediaController = mediaController, scope)
    }
}

/**
 * Represents the Play button to be displayed on the
 * [com.github.goldy1992.mp3player.client.ui.PlayToolbar].
 */
@Composable
fun PlayButton(mediaController : MediaControllerAdapter, scope : CoroutineScope = rememberCoroutineScope()) {
    IconButton(
        onClick = { scope.launch {
            Log.i(LOG_TAG, "calling play")
            mediaController.play()}
        }) {
        Icon(
            Icons.Filled.PlayArrow,
            contentDescription = stringResource(id = R.string.play),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
/**
 * Represents the Pause button to be displayed on the
 * [com.github.goldy1992.mp3player.client.ui.PlayToolbar].
 */
@Composable
fun PauseButton(mediaController : MediaControllerAdapter,
scope: CoroutineScope = rememberCoroutineScope()) {
    IconButton(onClick = { scope.launch {  mediaController.pause()}}) {
        Icon(
            Icons.Filled.Pause,
            contentDescription = stringResource(id = R.string.pause),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}