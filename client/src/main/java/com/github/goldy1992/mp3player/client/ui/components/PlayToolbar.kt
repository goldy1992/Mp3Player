package com.github.goldy1992.mp3player.client.ui.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.models.PlaybackPositionEvent
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.ui.DEFAULT_WINDOW_CLASS_SIZE
import com.github.goldy1992.mp3player.client.ui.buttons.PlayPauseButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToNextButton
import com.github.goldy1992.mp3player.client.ui.buttons.SkipToPreviousButton
import com.github.goldy1992.mp3player.client.ui.components.seekbar.PlaybackPositionAnimation

//@Preview(uiMode = UI_MODE_NIGHT_MASK)
@Composable
fun SharedTransitionScope.PlayToolbar(
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    isPlayingProvider : () -> Boolean = {false},
    onClickPlay: () -> Unit = {},
    onClickPause: () -> Unit = {},
    onClickSkipNext: () -> Unit = {},
    onClickSkipPrevious: () -> Unit = {},
    onClickBar : () -> Unit = {},
    currentSongProvider : () -> Song = { Song.DEFAULT },
    playbackSpeedProvider : () ->  Float = {1.0f},
    playbackPositionProvider: () -> PlaybackPositionEvent = { PlaybackPositionEvent.DEFAULT},
    windowSizeClass: WindowSizeClass = DEFAULT_WINDOW_CLASS_SIZE,
) {
    val bottomAppBarDescr = stringResource(id = R.string.bottom_app_bar)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClickBar() }
            .semantics { contentDescription = bottomAppBarDescr },
        shape = RoundedCornerShape(10f)
    ) {
        val localDensity = LocalDensity.current
        Column(verticalArrangement = Arrangement.Top) {
            PlaybackPositionAnimation(
                isPlayingProvider = isPlayingProvider,
                currentSongProvider = currentSongProvider,
                playbackSpeedProvider = playbackSpeedProvider,
                playbackPositionProvider = playbackPositionProvider
            ) {
                LinearProgressIndicator(
                    progress = { it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                )
            }
            var height by remember { mutableStateOf(0.dp) }
            Row(
                Modifier
                    .weight(1f)
                    .onSizeChanged { height = (it.height.toFloat() / localDensity.density).dp },
                verticalAlignment = Alignment.CenterVertically) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SkipToPreviousButton(
                        modifier = Modifier.size(40.dp),//.border(BorderStroke(1.dp, Color.Red)),
                        onClick = onClickSkipPrevious
                    )
                    PlayPauseButton(
                        modifier = Modifier.size(60.dp),//.border(BorderStroke(1.dp, Color.Red)),
                        isPlaying = isPlayingProvider,
                        onClickPlay = onClickPlay,
                        onClickPause = onClickPause
                    )
                    SkipToNextButton(
                        modifier = Modifier.size(40.dp),//.border(BorderStroke(1.dp, Color.Red)),
                        onClick = onClickSkipNext)
                }

                val currentSong = currentSongProvider()
                Column(Modifier.weight(1f).padding(4.dp)) {
                    Text(currentSong.title, maxLines = 1, modifier = Modifier.basicMarquee(), style = MaterialTheme.typography.bodyMedium)
                    Text(currentSong.artist, maxLines = 1, modifier = Modifier.basicMarquee(), style = MaterialTheme.typography.bodySmall)
                }

                Row(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp, end = 4.dp, start = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    AlbumArtAsync(
                        uri = currentSong.albumArt,
                        contentDescription = currentSong.title,
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(currentSong.id),
                            animatedVisibilityScope = animatedVisibilityScope
                        )
                    )
                }
            }
        }
    }
}