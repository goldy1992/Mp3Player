package com.github.goldy1992.mp3player.client.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.Song
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
                onClickBar : () -> Unit = {},
                currentSongProvider : () -> Song = { Song.DEFAULT }
) {
    val bottomAppBarDescr = stringResource(id = R.string.bottom_app_bar)
    BottomAppBar(
        Modifier
            .clickable { onClickBar() }
            .semantics { contentDescription = bottomAppBarDescr },
    )
    {
        Row(Modifier.weight(0.8f)) {
            SkipToPreviousButton(onClick = onClickSkipPrevious)
            PlayPauseButton(isPlaying = isPlayingProvider,
                onClickPlay = onClickPlay,
                onClickPause = onClickPause)
            SkipToNextButton(onClick = onClickSkipNext)
        }
        Row(modifier = Modifier.weight(0.2f)
            .padding(top=12.dp, bottom = 12.dp, end=16.dp, start = 16.dp),
            horizontalArrangement = Arrangement.End) {

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
