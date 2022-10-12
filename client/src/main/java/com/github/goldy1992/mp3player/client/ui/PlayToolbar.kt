package com.github.goldy1992.mp3player.client.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.github.goldy1992.mp3player.client.AsyncPlayerListener
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.ui.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToNextButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToPreviousButton
import kotlinx.coroutines.CoroutineScope

@Composable
fun PlayToolbar(mediaController : MediaControllerAdapter,
                isPlayingFlow: IsPlayingFlow,
                scope: CoroutineScope = rememberCoroutineScope(),
                onClick : () -> Unit) {
    val bottomAppBarDescr = stringResource(id = R.string.bottom_app_bar)
    BottomAppBar(
        modifier = Modifier
            .height(BOTTOM_BAR_SIZE)
            .clickable {
                onClick()
            }
            .semantics { contentDescription = bottomAppBarDescr })
    {
        Row(horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
                ) {
            SkipToPreviousButton(mediaController = mediaController, scope=scope)
            PlayPauseButton(mediaController = mediaController, isPlayingFlow = isPlayingFlow, scope=scope)
            SkipToNextButton(mediaController = mediaController, scope=scope)
        }
    }
}
