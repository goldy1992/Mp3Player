package com.github.goldy1992.mp3player.client.ui.buttons

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar

private const val LOG_TAG = "AlbumPlayPauseButton"

/**
 * This button will display the [PlayButton] by default if no implementation of isPlaying is
 * provided. there is currently
 * no playback, otherwise it will display the [PauseButton].
 * @param isPlaying Returns the current playback status, defaults to false.
 * @param onClickPlay Called when play is clicked, defaults to no implementation.
 * @param onClickPause Called when pause is clicked, defaults to no implementation.
 */
@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun AlbumPlayPauseButton(
                    modifier: Modifier = Modifier,
                    isPlaying : () -> Boolean = {false},
                    onClickPlay: () -> Unit = {},
                    onClickPause: () -> Unit = {}
) {
    val isPlayingValue = isPlaying()
    Log.d(LOG_TAG, "AlbumPlayPauseButton() isPlayingValue: $isPlayingValue")
    val tweenTime = 500
    val rotation by animateFloatAsState(targetValue = if (isPlayingValue) 180f else 0f, tween(tweenTime))

    val fadeTime = 300

    AnimatedContent(targetState = isPlayingValue,
        transitionSpec = {
            ContentTransform(
                targetContentEnter = fadeIn(tween(fadeTime)),
                initialContentExit = fadeOut(tween(fadeTime)),
            )
        }


        ) { isPlayingCurrentVal ->
        if (isPlayingCurrentVal) {
            AlbumPauseButton(onClick = onClickPause,
                modifier = modifier.rotate(rotation + 180f))
        } else {
            AlbumPlayButton(onClick = onClickPlay,
                modifier = modifier.rotate(rotation))
        }
    }

}

/**
 * Represents the Play button to be displayed on the [PlayToolbar].
 */
@Composable
fun AlbumPlayButton(modifier : Modifier = Modifier,
               onClick : () -> Unit =  {}) {
    IconButton(
        onClick = { onClick()},
        modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.PlayCircle,
            contentDescription = stringResource(id = R.string.play_album),
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier
        )
    }
}

/**
 * Represents the Pause button to be displayed on the [PlayToolbar].
 */
@Composable
fun AlbumPauseButton(modifier: Modifier = Modifier,
                onClick: () -> Unit = {},
                ) {
    IconButton(onClick = { onClick() },
                modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.PauseCircle,
            contentDescription = stringResource(id = R.string.pause_album),
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier
        )
    }
}