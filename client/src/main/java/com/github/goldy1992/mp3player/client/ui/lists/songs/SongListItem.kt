package com.github.goldy1992.mp3player.client.ui.lists.songs

import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.github.goldy1992.mp3player.client.ui.DEFAULT_PADDING
import com.github.goldy1992.mp3player.client.ui.RAINBOW_COLORS
import com.github.goldy1992.mp3player.client.ui.WindowSize
import com.github.goldy1992.mp3player.client.ui.components.Equalizer
import com.github.goldy1992.mp3player.client.ui.getWindowSizeClass
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getEmptyMediaItem

@ExperimentalCoilApi
@Preview
@ExperimentalFoundationApi
@Composable
fun SongListItem(song : MediaItem = getEmptyMediaItem(),
                 isPlaying : Boolean = false,
                 isSelected : Boolean = false,
                 onClick: (item : MediaItem) -> Unit = {}) {
    Column {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .background(if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.12f) else MaterialTheme.colors.background)
                .combinedClickable(
                    onClick = { onClick(song) },
                    onLongClick = {
                    })
                .padding(start = DEFAULT_PADDING, end = DEFAULT_PADDING),
            contentAlignment = Alignment.Center
        ) {

            when (getWindowSizeClass(DpSize(maxWidth, maxHeight))) {
                WindowSize.Expanded -> ExpandedSongItem(song, isPlaying, isSelected)
                WindowSize.Compact -> CompactSongItem(song, isPlaying, isSelected)
                else -> MediumSongItem(song, isPlaying, isSelected)

            }
        }
        Divider()
    }
}

@ExperimentalCoilApi
@Preview
@Composable
private fun ExpandedSongItem(
    song: MediaItem = getEmptyMediaItem(),
    isPlaying: Boolean = false,
    isSelected: Boolean = false
) {
    val backgroundColor = if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.12f) else MaterialTheme.colors.background
    Row(
        modifier = Modifier
            .background(backgroundColor)
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Box(
            Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            contentAlignment = Alignment.CenterStart
            ) {
            AlbumArt(song = song)
        }
        Box(
            Modifier
                .weight(3f)
                .align(Alignment.CenterVertically)) {
            Text(
                text = MediaItemUtils.getTitle(song),
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Box(
            Modifier
                .weight(3f)
                .align(Alignment.CenterVertically)
        ) {

            Text(
                text = MediaItemUtils.getArtist(song)!!,
                maxLines = 1,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .align(Alignment.CenterVertically)
        ) {
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
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Right
            )
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "")
            }
        }
    }
}

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Preview
@Composable
private fun CompactSongItem(
    song: MediaItem = getEmptyMediaItem(),
    isPlaying: Boolean = false,
    isSelected: Boolean = false,
    onClick: (item: MediaItem) -> Unit = {}
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.12f) else MaterialTheme.colors.background)
        .combinedClickable(
            onClick = { onClick(song) },
            onLongClick = {
            }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AlbumArt(song)
        Column(verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = DEFAULT_PADDING)) {
            Text(text = MediaItemUtils.getTitle(song),
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)

            Text(
                text = MediaItemUtils.getArtist(song)!!,
                maxLines = 1,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis)
        }
        Row (horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = DEFAULT_PADDING)
        ) {
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
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Right
            )
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "")
            }


        }
    }
}

@ExperimentalFoundationApi
@Preview
@Composable
private fun MediumSongItem(
    song: MediaItem = getEmptyMediaItem(),
    isPlaying: Boolean = false,
    isSelected: Boolean = false,
    onClick: (item: MediaItem) -> Unit = {}
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(if (isSelected) MaterialTheme.colors.primary.copy(alpha = 0.12f) else MaterialTheme.colors.background)
        .combinedClickable(
            onClick = { onClick(song) },
            onLongClick = {
            }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AlbumArt(song)
        Column(verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = DEFAULT_PADDING)) {
            Text(text = MediaItemUtils.getTitle(song),
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)

            Text(
                text = MediaItemUtils.getArtist(song)!!,
                maxLines = 1,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis)
        }
        Row (horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = DEFAULT_PADDING)
        ) {
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
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Right
            )
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "")
            }


        }
    }
}

@ExperimentalCoilApi
@Composable
private fun AlbumArt(song: MediaItem) {
    Image(
        modifier = Modifier
            .size(60.dp, 60.dp)
            .padding(1.dp),
        painter = rememberImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(MediaItemUtils.getAlbumArtUri(song = song)).build(),
        ),
        contentDescription = ""
    )
}

