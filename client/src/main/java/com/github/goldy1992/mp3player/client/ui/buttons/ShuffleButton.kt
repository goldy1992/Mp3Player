package com.github.goldy1992.mp3player.client.ui.buttons

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ShuffleOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.flows.player.ShuffleModeFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * This button will display the either [ShuffleOnButton] or the [ShuffleOffButton] depending on the
 * current shuffle mode indicated by the [MediaControllerAdapter].
 */
@Composable
fun ShuffleButton(mediaController: MediaControllerAdapter,
                 shuffleModeFlow: ShuffleModeFlow,
                 scope: CoroutineScope = rememberCoroutineScope()) {
    val shuffleMode by shuffleModeFlow.state.collectAsState()
    if (shuffleMode) {
        ShuffleOnButton(mediaController = mediaController, scope = scope)
    } else {
        ShuffleOffButton(mediaController = mediaController, scope = scope)
    }
}

/**
 * Represents the Shuffle On Button
 */
@Composable
fun ShuffleOnButton(mediaController: MediaControllerAdapter,
                    scope: CoroutineScope = rememberCoroutineScope()) {
    IconButton(onClick = {scope.launch {mediaController.setShuffleMode(false)} })
    {
        Icon(Icons.Filled.ShuffleOn, contentDescription = stringResource(id = R.string.shuffle_on),
            tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
/**
 * Represents the Shuffle Off Button
 */
@Composable
fun ShuffleOffButton(mediaController: MediaControllerAdapter,
                     scope: CoroutineScope = rememberCoroutineScope()) {
    IconButton(onClick = { scope.launch { mediaController.setShuffleMode(true)} })
    {
        Icon(Icons.Filled.Shuffle, contentDescription = stringResource(id = R.string.shuffle_off),
            tint = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}