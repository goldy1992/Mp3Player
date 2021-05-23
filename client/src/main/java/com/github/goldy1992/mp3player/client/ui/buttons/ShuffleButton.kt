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
import com.github.goldy1992.mp3player.client.MediaControllerAdapter

@Composable
fun ShuffleButton(mediaController: MediaControllerAdapter) {
    val shuffleMode by mediaController.shuffleMode.observeAsState()
    if (shuffleMode == PlaybackStateCompat.SHUFFLE_MODE_ALL) {
        ShuffleOnButton(mediaController = mediaController)
    } else {
        ShuffleOffButton(mediaController = mediaController)
    }
}

@Composable
fun ShuffleOnButton(mediaController: MediaControllerAdapter) {
    IconButton(onClick = {mediaController.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_NONE)})
    {
        Icon(Icons.Filled.ShuffleOn, contentDescription = "Shuffle On")
    }
}

@Composable
fun ShuffleOffButton(mediaController: MediaControllerAdapter) {
    IconButton(onClick = {mediaController.setShuffleMode(PlaybackStateCompat.SHUFFLE_MODE_ALL)})
    {
        Icon(Icons.Filled.Shuffle, contentDescription = "Shuffle Off")
    }
}