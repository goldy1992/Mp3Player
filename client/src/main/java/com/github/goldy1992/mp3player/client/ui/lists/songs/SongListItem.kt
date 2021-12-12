package com.github.goldy1992.mp3player.client.ui.lists.songs

import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.github.goldy1992.mp3player.client.ui.DEFAULT_PADDING
import com.github.goldy1992.mp3player.client.ui.RAINBOW_COLORS
import com.github.goldy1992.mp3player.client.ui.components.Equalizer
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.MediaItemUtils

@ExperimentalCoilApi
@Preview
@ExperimentalFoundationApi
@Composable
fun SongListItem(song : MediaItem = MediaItemUtils.getEmptyMediaItem(),
                 isPlaying : Boolean = true,
                 onClick: (item : MediaItem) -> Unit = {}) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .combinedClickable(
                        onClick = { onClick(song) },
                        onLongClick = {
                        })
                .padding(start = DEFAULT_PADDING, end = DEFAULT_PADDING),
                verticalAlignment = Alignment.CenterVertically,
        ) {
                Image(modifier = Modifier
                        .size(50.dp, 50.dp)
                        .padding(1.dp),
                        painter = rememberImagePainter(
                          ImageRequest.Builder(LocalContext.current).data(MediaItemUtils.getAlbumArtUri(song = song)).build(),
                        ),
                        contentDescription = "")
                Column(verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(start = DEFAULT_PADDING)) {
                        Text(text = MediaItemUtils.getTitle(song),
                                style = MaterialTheme.typography.body2,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis)

                        Text(
                             //   modifier = Modifier.weight(9f),
                                text = MediaItemUtils.getArtist(song)!!,
                                maxLines = 1,
                                style = MaterialTheme.typography.caption,
                                overflow = TextOverflow.Ellipsis)
                }
                Row (horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.fillMaxWidth()
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
                }
        }

}


