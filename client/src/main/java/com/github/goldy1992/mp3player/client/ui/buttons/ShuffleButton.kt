package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.goldy1992.mp3player.client.R

/**
 * This button will display the either [ShuffleOnButton] or the [ShuffleOffButton] depending on the
 * current shuffle mode indicated by the shuffleEnabledProvider.
 * @param shuffleEnabledProvider Provides the current shuffle mode, i.e. enabled/disabled.
 * @param onClick The code to be invoked when the button is pressed.
 */
@Composable
fun ShuffleButton(shuffleEnabledProvider : () -> Boolean,
        onClick : (isEnabled : Boolean) -> Unit) {
    val isShuffleEnabled = shuffleEnabledProvider()
    if (isShuffleEnabled) {
        ShuffleOnButton(onClick = {onClick(false)})
    } else {
        ShuffleOffButton(onClick = { onClick(true) })
    }
}
/**
 * Represents the Shuffle On Button
 */
@Composable
fun ShuffleOnButton(onClick : () -> Unit = {}) {
    IconButton(onClick = onClick)
    {
        Icon(Icons.Filled.Shuffle, contentDescription = stringResource(id = R.string.shuffle_on),
            tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/**
 * Represents the Shuffle Off Button
 */
@Composable
fun ShuffleOffButton(onClick : () -> Unit = {}) {
    IconButton(onClick = onClick)
    {
        Icon(Icons.Filled.Shuffle, contentDescription = stringResource(id = R.string.shuffle_off),
            tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}