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

    var pauseRotation by remember { mutableStateOf(Animatable(if (isPlayingValue) 0f else 180f)) }
    var playRotation by remember { mutableStateOf(Animatable(if (!isPlayingValue) 0f else 180f)) }

    val tweenTime = 1000
    AnimatedContent(targetState = isPlayingValue,
        transitionSpec = {
            ContentTransform(
                targetContentEnter = fadeIn(tween(tweenTime)),
                initialContentExit = fadeOut(tween(tweenTime)),
            )
        }


        ) { isPlayingCurrentVal ->
        if (isPlayingCurrentVal) {
            PauseButton(onClickPause,
                Modifier.rotate(pauseRotation.value)) {
                pauseRotation =  Animatable(0f)
                playRotation = Animatable(180f)
                scope.launch { playRotation.animateTo(playRotation.value + 180f, tween(tweenTime)) }
                scope.launch { pauseRotation.animateTo(pauseRotation.value + 180f, tween(tweenTime)) }
            }
        } else {
            PlayButton(onClickPlay,
                Modifier.rotate(playRotation.value)) {
                pauseRotation = Animatable(180f)
                playRotation = Animatable(0f)
                scope.launch { playRotation.animateTo(playRotation.value + 180f, tween(1000)) }
                scope.launch { pauseRotation.animateTo(pauseRotation.value + 180f, tween(1000)) }
            }
        }
    }
}

/**
 * Represents the Play button to be displayed on the [PlayToolbar].
 */
@Composable
fun PlayButton(onClick : () -> Unit =  {},
               modifier : Modifier = Modifier,
               onClickAnimationUpdate: () -> Unit = {}) {
    IconButton(
        onClick = { onClick()
        onClickAnimationUpdate()},
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
                modifier: Modifier = Modifier,
                onClickAnimationUpdate: () -> Unit = {}) {
    IconButton(onClick = {
        onClick()
        onClickAnimationUpdate()
    },
    modifier = modifier) {
        Icon(
            Icons.Filled.Pause,
            contentDescription = stringResource(id = R.string.pause),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}