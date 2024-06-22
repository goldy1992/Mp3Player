package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ShuffleOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.R

/**
 * This button will display the either [ShuffleOnButton] or the [ShuffleOffButton] depending on the
 * current shuffle mode indicated by the shuffleEnabledProvider.
 * @param isShuffleEnabled Provides the current shuffle mode, i.e. enabled/disabled.
 * @param onClick The code to be invoked when the button is pressed.
 */
@Preview
@Composable
fun ShuffleButton(
    modifier : Modifier = Modifier,
    isShuffleEnabled: Boolean = true,
    onClick : (isEnabled : Boolean) -> Unit = {}) {
    val fadeTime = 300
    AnimatedContent(
        targetState = isShuffleEnabled,
        transitionSpec = {
            ContentTransform(
                targetContentEnter = fadeIn(tween(fadeTime)),
                initialContentExit = fadeOut(tween(fadeTime)),
            )
        }, label = "ShuffleButtonAnimation"
    ) { shuffleEnabled ->
        if (shuffleEnabled) {
            ShuffleOnButton(
                modifier = modifier,
                onClick = { onClick(false) }
            )
        } else {
            ShuffleOffButton(
                modifier = modifier,
                onClick = { onClick(true) }
            )
        }
    }
}
/**
 * Represents the Shuffle On Button
 */
@Composable
fun ShuffleOnButton(
    modifier: Modifier = Modifier,
    onClick : () -> Unit = {}) {
    IconButton(
        onClick = onClick,
        modifier = modifier)
    {
        Icon(Icons.Filled.ShuffleOn, contentDescription = stringResource(id = R.string.shuffle_on),
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier)
    }
}

/**
 * Represents the Shuffle Off Button
 */
@Composable
fun ShuffleOffButton(
    modifier: Modifier = Modifier,
    onClick : () -> Unit = {}
) {
    IconButton(
        modifier = modifier,
        onClick = onClick)
    {
        Icon(Icons.Filled.Shuffle, contentDescription = stringResource(id = R.string.shuffle_off),
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier)
    }
}