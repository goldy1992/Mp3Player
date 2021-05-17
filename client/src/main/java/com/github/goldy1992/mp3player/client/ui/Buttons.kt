package com.github.goldy1992.mp3player.client.ui

import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.painterResource
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R


@Composable
fun PlayPauseButton(mediaController: MediaControllerAdapter) {
    val isPlaying by mediaController.isPlaying.observeAsState()
    if (isPlaying!!) {
        pauseButton(mediaController = mediaController)
    } else {
        playButton(mediaController = mediaController)
    }
}



@Composable
fun playButton(mediaController : MediaControllerAdapter) {
    Button(onClick = { mediaController.play()}) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_play_arrow_24px),
            contentDescription = "Play"
        )
    }
}

@Composable
fun pauseButton(mediaController : MediaControllerAdapter) {
    Button(onClick = { mediaController.pause()}) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_pause_24px),
            contentDescription = "Pause"
        )
    }
}