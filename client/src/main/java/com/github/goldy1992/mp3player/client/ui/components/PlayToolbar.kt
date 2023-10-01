package com.github.goldy1992.mp3player.client.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.ui.DEFAULT_WINDOW_CLASS_SIZE
import com.github.goldy1992.mp3player.client.ui.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToNextButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToPreviousButton

@Preview
@Composable
fun PlayToolbar(
    isPlayingProvider : () -> Boolean = {false},
    onClickPlay: () -> Unit = {},
    onClickPause: () -> Unit = {},
    onClickSkipNext: () -> Unit = {},
    onClickSkipPrevious: () -> Unit = {},
    onClickBar : () -> Unit = {},
    currentSongProvider : () -> Song = { Song.DEFAULT },
    windowSizeClass: WindowSizeClass = DEFAULT_WINDOW_CLASS_SIZE,
    progressIndicator : @Composable () -> Unit = {}
) {
    val bottomAppBarDescr = stringResource(id = R.string.bottom_app_bar)
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClickBar() }
            .semantics { contentDescription = bottomAppBarDescr },
        shape = RoundedCornerShape(10f)
    )
    {
        Column(verticalArrangement = Arrangement.Top) {
            progressIndicator()

            Row(verticalAlignment = Alignment.CenterVertically) {
                Row(Modifier.weight(0.8f)) {
                    SkipToPreviousButton(
                        onClick = onClickSkipPrevious
                    )
                    PlayPauseButton(
                        isPlaying = isPlayingProvider,
                        onClickPlay = onClickPlay,
                        onClickPause = onClickPause
                    )
                    SkipToNextButton(onClick = onClickSkipNext)
                }
                Row(
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(top = 12.dp, bottom = 12.dp, end = 16.dp, start = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {

                    val currentSong = currentSongProvider()
                    if (currentSong != Song.DEFAULT) {
                        AlbumArtAsync(
                            uri = currentSong.albumArt,
                            contentDescription = currentSong.title,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
