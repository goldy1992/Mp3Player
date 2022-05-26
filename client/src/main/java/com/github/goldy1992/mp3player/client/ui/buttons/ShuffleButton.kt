package com.github.goldy1992.mp3player.client.ui.buttons

import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.ShuffleOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R

/**
 * This button will display the either [ShuffleOnButton] or the [ShuffleOffButton] depending on the
 * current shuffle mode indicated by the [MediaControllerAdapter].
 */
@Composable
fun ShuffleButton(mediaController: MediaControllerAdapter) {
    val shuffleMode by mediaController.shuffleMode.observeAsState()
    if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL) {
        ShuffleOnButton(mediaController = mediaController)
    } else {
        ShuffleOffButton(mediaController = mediaController)
    }
}

/**
 * Represents the Shuffle On Button
 */
@Composable
fun ShuffleOnButton(mediaController: MediaControllerAdapter) {
    IconButton(onClick = {mediaController.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE)})
    {
        Icon(Icons.Filled.ShuffleOn, contentDescription = stringResource(id = R.string.shuffle_on))
    }
}
/**
 * Represents the Shuffle Off Button
 */
@Composable
fun ShuffleOffButton(mediaController: MediaControllerAdapter) {
    IconButton(onClick = {mediaController.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)})
    {
        Icon(Icons.Filled.Shuffle, contentDescription = stringResource(id = R.string.shuffle_off))
    }
}