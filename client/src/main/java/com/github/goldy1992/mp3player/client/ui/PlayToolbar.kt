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
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToNextButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToPreviousButton
import com.github.goldy1992.mp3player.commons.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Composable
fun PlayToolbar(mediaController : MediaControllerAdapter,
                isPlayingState: StateFlow<Boolean>,
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
            PlayPauseButton(mediaController = mediaController, isPlayingState = isPlayingState, scope=scope)
            SkipToNextButton(mediaController = mediaController, scope=scope)
        }
    }
}


@Preview
@Composable
fun PlayToolbar(isPlaying : () -> Boolean = {false} ,
                onClickPlaying: () -> Unit = {},
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
            PlayPauseButton(isPlaying = isPlaying,
                            onClickPlay = onClickPlaying,
                            onClickPause = onClickPause)
            SkipToNextButton(onClick = onClickSkipNext)
        }
    }
}

@Composable
fun PlayToolbar(isPlayingProvider : () -> Boolean = {false},
                mediaController : MediaControllerAdapter,
                navController: NavController,
                scope : CoroutineScope) {
    PlayToolbar(isPlaying = isPlayingProvider,
        onClickPause = { scope.launch { mediaController.pause() } },
        onClickPlaying = { scope.launch { mediaController.play() } },
        onClickSkipNext = { scope.launch { mediaController.skipToNext() } },
        onClickSkipPrevious = { scope.launch { mediaController.skipToPrevious() } }
    ) {
        navController.navigate(Screen.NOW_PLAYING.name)
    }

}
