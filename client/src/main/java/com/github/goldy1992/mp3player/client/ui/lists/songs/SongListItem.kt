package com.github.goldy1992.mp3player.client.ui.lists.songs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.models.media.Song
import com.github.goldy1992.mp3player.client.ui.components.AlbumArtAsync
import com.github.goldy1992.mp3player.client.utils.TimeUtils.formatTime

private const val logTag = "SongListItem"


@ExperimentalCoilApi
@ExperimentalFoundationApi
@Preview
@Composable
fun SongListItem(
    song : Song = Song(),
    isSelected : Boolean = false,
    isPlayingProvider : Boolean = false,
    expanded : Boolean = true,
    onClick: () -> Unit = {},
    containerColor : Color = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
) {
    ListItem(
        modifier = Modifier
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { }
            )
            .requiredHeight(72.dp),
        colors = ListItemDefaults.colors(containerColor = containerColor) ,
            headlineContent = {
                Text(
                text = song.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )},
        leadingContent = {
//                // TODO: Move equalizer to overlay song album art image
//                if (isPlaying) {
//                    Equalizer(
//                        maxHeight = 20.dp,
//                        numOfBars = 4,
//                        barColors = RAINBOW_COLORS,
//                        barWidth = 5.dp
//                    )
//                }

            AlbumArtAsync(uri = song.albumArt, contentDescription = song.title)//, contentDescription = song.title, modifier = Modifier.size(40.dp))

         },
        supportingContent = {
            Text(
                text = song.artist,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis
            )

        },
        trailingContent = {
            Text(
                modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                text = formatTime(song.duration),
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        )
    HorizontalDivider(//startIndent = 72.dp,
        color = MaterialTheme.colorScheme.surfaceVariant)
}


@ExperimentalCoilApi
@ExperimentalFoundationApi
@Preview
@Composable
fun LargeSongListItem(
    song : Song = Song(),
    isSelected : Boolean = false,
    isPlayingProvider : () -> Boolean = {false},
    expanded : Boolean = true,
    onClick: () -> Unit = {},
    containerColor : Color = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
) {
    ListItem(
        modifier = Modifier
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { }
            )
            .requiredHeight(72.dp),
        colors = ListItemDefaults.colors(containerColor = containerColor) ,
        headlineContent = {
            Row(
                Modifier
                    .fillMaxSize()
                    ) {
                Column(Modifier.fillMaxHeight().weight(4f),//.background(Color.Green),
                    verticalArrangement = Arrangement.Center) {
                    Text(
                        text = song.title,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Column(Modifier.fillMaxHeight().weight(4f),//.background(Color.Blue),
                    verticalArrangement = Arrangement.Center
                )  {
                    Text(
                        text = song.artist,
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Column(Modifier.fillMaxHeight().weight(1f),//.background(Color.Red),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End)  {
                    Text(
                        //    modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                        text = formatTime(song.duration),
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        },
        leadingContent = {
//                val isPlaying = isPlayingProvider()
//                // TODO: Move equalizer to overlay song album art image
//                if (isPlaying) {
//                    Equalizer(
//                        maxHeight = 20.dp,
//                        numOfBars = 4,
//                        barColors = RAINBOW_COLORS,
//                        barWidth = 5.dp
//                    )
//                }

            AlbumArtAsync(uri = song.albumArt, contentDescription = song.title)//, contentDescription = song.title, modifier = Modifier.size(40.dp))

        },
        supportingContent = { Surface(color = Color.Blue) {}
        },
    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.surfaceVariant
    )
}


