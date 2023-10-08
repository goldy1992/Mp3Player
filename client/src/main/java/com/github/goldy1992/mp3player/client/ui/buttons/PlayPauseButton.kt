package com.github.goldy1992.mp3player.client.ui.buttons

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.components.PlayToolbar

private const val LOG_TAG = "PlayPauseButton"

/**
 * This button will display the [PlayButton] by default if no implementation of isPlaying is
 * provided. there is currently
 * no playback, otherwise it will display the [PauseButton].
 * @param isPlaying Returns the current playback status, defaults to false.
 * @param onClickPlay Called when play is clicked, defaults to no implementation.
 * @param onClickPause Called when pause is clicked, defaults to no implementation.
 */
@Composable
fun PlayPauseButton(
                    modifier: Modifier = Modifier,
                    isPlaying : () -> Boolean = {false},
                    onClickPlay: () -> Unit = {},
                    onClickPause: () -> Unit = {}
) {
    val isPlayingValue = isPlaying()
    Log.d(LOG_TAG, "PlayPauseButton() isPlayingValue: $isPlayingValue")
    val tweenTime = 500
    val rotation by animateFloatAsState(targetValue = if (isPlayingValue) 180f else 0f, tween(tweenTime),
        label = "playPause Rotation Animation"
    )

    val fadeTime = 300

    AnimatedContent(targetState = isPlayingValue,
        transitionSpec = {
            ContentTransform(
                targetContentEnter = fadeIn(tween(fadeTime)),
                initialContentExit = fadeOut(tween(fadeTime)),
            )
        }, label = "PlayPause value changing"


    ) { isPlayingCurrentVal ->
        if (isPlayingCurrentVal) {
            PauseButton(onClick = onClickPause,
                modifier = modifier.rotate(rotation + 180f))
        } else {
            PlayButton(onClick = onClickPlay,
                modifier = modifier.rotate(rotation))
        }
    }

}

/**
 * Represents the Play button to be displayed on the [PlayToolbar].
 */
@Composable
fun PlayButton(modifier : Modifier = Modifier,
               onClick : () -> Unit =  {}) {
    IconButton(
        colors = IconButtonDefaults.filledTonalIconButtonColors(),//containerColor = MaterialTheme.colorScheme.inversePrimary),
        onClick = { onClick()},
        modifier = modifier){//.background(Color.Yellow)) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            imageVector = Icons.Rounded.PlayArrow,
            contentDescription = stringResource(id = R.string.play),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}

/**
 * Represents the Pause button to be displayed on the [PlayToolbar].
 */
@Composable
fun PauseButton(modifier: Modifier = Modifier,
                onClick: () -> Unit = {},
                ) {
    IconButton(
        colors = IconButtonDefaults.filledTonalIconButtonColors(),//containerColor = MaterialTheme.colorScheme.inversePrimary),
        onClick = { onClick() },
        modifier = modifier) {
        Icon(
            modifier = Modifier.fillMaxSize(),
            imageVector = Icons.Rounded.Pause,
            contentDescription = stringResource(id = R.string.pause),
            tint = MaterialTheme.colorScheme.primary,
        )
    }
}