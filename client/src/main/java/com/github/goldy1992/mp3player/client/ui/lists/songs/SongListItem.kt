package com.github.goldy1992.mp3player.client.ui.lists.songs

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getEmptyMediaItem

private const val logTag = "SongListItem"


@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalCoilApi
@ExperimentalFoundationApi
@Preview
@Composable
fun SongListItem(song : MediaItem = getEmptyMediaItem(),
                 isSelected : Boolean = false,
                 onClick: () -> Unit = {}) {
    Log.i(logTag, "isSelected: $isSelected, songId: ${song.mediaId}, title: ${song.mediaMetadata.title}")
    val containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
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
                text = MediaItemUtils.getTitle(song),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )},
            leadingContent = { AlbumArt(song = (song)) },
            supportingText = {
                Text(
                    text = MediaItemUtils.getArtist(song),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis
                )

            },
            trailingContent = {
                Text(
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                    text = formatTime(MediaItemUtils.getDuration(song)),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
//                    // TODO: Move equalizer to overlay song album art image
//                    if (isPlaying) {
//                        Equalizer(
//                            maxHeight = 20.dp,
//                            numOfBars = 4,
//                            barColors = RAINBOW_COLORS,
//                            barWidth = 5.dp
//                        )
//                    }
        )
    Divider(//startIndent = 72.dp,
        color = MaterialTheme.colorScheme.surfaceVariant)
}

@ExperimentalCoilApi
@Composable
private fun AlbumArt(song: MediaItem) {

    val uri : Uri? = MediaItemUtils.getAlbumArtUri(song)
    if (uri != null) {
        Image(
            modifier = Modifier
                .size(40.dp, 40.dp),
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(MediaItemUtils.getAlbumArtUri(song = song)).build()
            ),
            contentDescription = ""
        )
    }
    else {
        Icon(Icons.Filled.QuestionAnswer, contentDescription = "", modifier = Modifier.size(40.dp))
    }
}

