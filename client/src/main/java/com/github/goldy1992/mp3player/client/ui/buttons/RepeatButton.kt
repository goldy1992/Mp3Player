package com.github.goldy1992.mp3player.client.ui.buttons

import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOn
import androidx.compose.material.icons.filled.RepeatOneOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.github.goldy1992.mp3player.client.MediaControllerAdapter

@Composable
fun RepeatButton(mediaController : MediaControllerAdapter) {
    val repeatMode by mediaController.repeatMode.observeAsState()
    when (repeatMode) {
        PlaybackStateCompat.REPEAT_MODE_ONE -> RepeatOneButton(mediaController = mediaController)
        PlaybackStateCompat.REPEAT_MODE_ALL -> RepeatAllButton(mediaController = mediaController)
        else -> RepeatNoneButton(mediaController = mediaController)
    }
}

@Composable
fun RepeatOneButton(mediaController : MediaControllerAdapter) {
    IconButton(onClick = {mediaController.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ALL)}) {
        Icon(Icons.Filled.RepeatOneOn, contentDescription = "Repeat One Button")
    }
}

@Composable
fun RepeatAllButton(mediaController : MediaControllerAdapter) {
    IconButton(onClick = {mediaController.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_NONE)}) {
        Icon(Icons.Filled.RepeatOn, contentDescription = "Repeat All Button")
    }
}

@Composable
fun RepeatNoneButton(mediaController : MediaControllerAdapter) {
   IconButton(onClick = {mediaController.setRepeatMode(PlaybackStateCompat.REPEAT_MODE_ONE)}) {
       Icon(Icons.Filled.Repeat, contentDescription = "Repeat None Button")
   }
}

