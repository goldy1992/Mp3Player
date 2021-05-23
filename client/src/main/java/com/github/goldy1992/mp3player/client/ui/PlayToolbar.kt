package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToNextButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToPreviousButton

@Composable
fun PlayToolbar(mediaController : MediaControllerAdapter, onClick : () -> Unit) {
    BottomAppBar(
        modifier = Modifier
            .height(BOTTOM_BAR_SIZE)
            .clickable { onClick() }) {
        SkipToPreviousButton(mediaController = mediaController)
        PlayPauseButton(mediaController = mediaController)
        SkipToNextButton(mediaController = mediaController)
    }

}
