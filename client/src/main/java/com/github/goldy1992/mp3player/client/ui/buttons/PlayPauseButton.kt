package com.github.goldy1992.mp3player.client.ui.buttons

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val logTag = "PlayPauseButton"

/**
 * This button will display the [PlayButton] by default if no implementation of isPlaying is
 * provided. there is currently
 * no playback, otherwise it will display the [PauseButton].
 * @param isPlaying Returns the current playback status, defaults to false.
 * @param onClickPlay Called when play is clicked, defaults to no implementation.
 * @param onClickPause Called when pause is clicked, defaults to no implementation.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayPauseButton(isPlaying : () -> Boolean = {false},
                    onClickPlay: () -> Unit = {},
                    onClickPause: () -> Unit = {},
                    scope : CoroutineScope = rememberCoroutineScope()
) {
    val isPlayingValue = isPlaying()
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
            PauseButton(onClickPause,
                Modifier.rotate(rotation + 180f))
        } else {
            PlayButton(onClickPlay,
                Modifier.rotate(rotation))
        }
    }

}

/**
 * Represents the Play button to be displayed on the [PlayToolbar].
 */
@Composable
fun PlayButton(onClick : () -> Unit =  {},
               modifier : Modifier = Modifier) {
    IconButton(
        onClick = { onClick()},
        modifier = modifier) {
        Icon(
            Icons.Filled.PlayArrow,
            contentDescription = stringResource(id = R.string.play),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Represents the Pause button to be displayed on the [PlayToolbar].
 */
@Composable
fun PauseButton(onClick: () -> Unit,
                modifier: Modifier = Modifier) {
    IconButton(onClick = { onClick() },
    modifier = modifier) {
        Icon(
            Icons.Filled.Pause,
            contentDescription = stringResource(id = R.string.pause),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}