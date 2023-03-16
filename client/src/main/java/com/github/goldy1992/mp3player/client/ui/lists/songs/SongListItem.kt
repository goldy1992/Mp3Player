package com.github.goldy1992.mp3player.client.ui.lists.songs

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.github.goldy1992.mp3player.client.R.drawable.cd_image1
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime

private const val logTag = "SongListItem"


@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalCoilApi
@ExperimentalFoundationApi
@Preview
@Composable
fun SongListItem(song : Song = Song(),
                 isSelected : Boolean = false,
                 isPlayingProvider : () -> Boolean = {false},
                 onClick: () -> Unit = {},
                containerColor : Color = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface) {
    //Log.i(logTag, "isSelected: $isSelected, songId: ${song.id}, title: ${song.title}")
    ListItem(
            modifier = Modifier
                .combinedClickable(
                    onClick = { onClick() },
                    onLongClick = { }
                )
                .requiredHeight(72.dp),
        colors = ListItemDefaults.colors(containerColor = containerColor) ,
            headlineText = {
                Text(
                text = song.title,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )},
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

                AlbumArt(uri = song.albumArt, modifier = Modifier.size(40.dp))

             },
            supportingText = {
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
    Divider(//startIndent = 72.dp,
        color = MaterialTheme.colorScheme.surfaceVariant)
}

@ExperimentalCoilApi
@Composable
fun AlbumArt(uri : Uri?,
            modifier : Modifier = Modifier) {
    if (uri != null) {

        Image(
            modifier = modifier,
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .placeholder(cd_image1)
                    .data(uri).build()
            ),
            contentDescription = ""
        )
    }
    else {
        Icon(Icons.Filled.QuestionAnswer, contentDescription = "", modifier = modifier)
    }
}

