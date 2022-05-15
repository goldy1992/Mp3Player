package com.github.goldy1992.mp3player.client.ui.lists.songs

import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.github.goldy1992.mp3player.client.ui.RAINBOW_COLORS
import com.github.goldy1992.mp3player.client.ui.components.Equalizer
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getEmptyMediaItem

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalCoilApi
@Preview
@ExperimentalFoundationApi
@Composable
fun SongListItem(song : MediaItem = getEmptyMediaItem(),
                 isPlaying : Boolean = false,
                 isSelected : Boolean = false,
                 onClick: (item : MediaItem) -> Unit = {}) {
    ListItem(
        modifier = Modifier.combinedClickable(
            onClick = { onClick(song) },
            onLongClick = { }
        ),
        icon = { AlbumArt(song = (song)) },
        secondaryText = {
            Text(
                text = MediaItemUtils.getArtist(song)!!,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis
            )

        },
        trailing = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isPlaying) {
                    Equalizer(
                        maxHeight = 20.dp,
                        numOfBars = 4,
                        barColors = RAINBOW_COLORS,
                        barWidth = 5.dp
                    )
                }
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = formatTime(MediaItemUtils.getDuration(song)),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                 )
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "",
                    tint = androidx.compose.material3.MaterialTheme.colorScheme.onSurface)
                }
            }

        }
    ) {
        Text(
            text = MediaItemUtils.getTitle(song),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@ExperimentalCoilApi
@Composable
private fun AlbumArt(song: MediaItem) {
    Image(
        modifier = Modifier
            .size(40.dp, 40.dp),
        painter = rememberImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(MediaItemUtils.getAlbumArtUri(song = song)).build(),
        ),
        contentDescription = ""
    )
}

