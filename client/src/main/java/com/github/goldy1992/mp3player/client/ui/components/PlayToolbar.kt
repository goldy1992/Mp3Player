package com.github.goldy1992.mp3player.client.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.BOTTOM_BAR_SIZE
import com.github.goldy1992.mp3player.client.ui.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToNextButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToPreviousButton

@Preview
@Composable
fun PlayToolbar(isPlayingProvider : () -> Boolean = {false},
                onClickPlay: () -> Unit = {},
                onClickPause: () -> Unit = {},
                onClickSkipNext: () -> Unit = {},
                onClickSkipPrevious: () -> Unit = {},
                onClickBar : () -> Unit = {}) {
    val bottomAppBarDescr = stringResource(id = R.string.bottom_app_bar)
    BottomAppBar(
        modifier = Modifier
            .height(BOTTOM_BAR_SIZE)
            .clickable {
                onClickBar()
            }
            .semantics { contentDescription = bottomAppBarDescr })
    {
        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            SkipToPreviousButton(onClick = onClickSkipPrevious)
            PlayPauseButton(isPlaying = isPlayingProvider,
                            onClickPlay = onClickPlay,
                            onClickPause = onClickPause)
            SkipToNextButton(onClick = onClickSkipNext)
        }
    }
}
