package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R

/**
 * This button will display the [PlayButton] if the [MediaControllerAdapter] says there is currently
 * no playback, otherwise it will display the [PauseButton].
 */
@Composable
fun PlayPauseButton(mediaController: MediaControllerAdapter) {
    val isPlaying by mediaController.isPlaying.observeAsState()
    if (isPlaying!!) {
        PauseButton(mediaController = mediaController)
    } else {
        PlayButton(mediaController = mediaController)
    }
}

/**
 * Represents the Play button to be displayed on the
 * [com.github.goldy1992.mp3player.client.ui.PlayToolbar].
 */
@Composable
fun PlayButton(mediaController : MediaControllerAdapter) {
    IconButton(
        onClick = { mediaController.play()}) {
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
fun PauseButton(mediaController : MediaControllerAdapter) {
    IconButton(onClick = { mediaController.pause()}) {
        Icon(
            Icons.Filled.Pause,
            contentDescription = stringResource(id = R.string.pause)
        )
    }
}