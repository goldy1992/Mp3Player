package com.github.goldy1992.mp3player.client.ui;

import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.google.accompanist.glide.rememberGlidePainter

@Composable
fun SongListItem(song : MediaItem,
                 onClick: () -> Unit) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .clickable(onClick = onClick)
                .padding(start = DEFAULT_PADDING, end = DEFAULT_PADDING),
                verticalAlignment = Alignment.CenterVertically,
        ) {
                Image(modifier = Modifier.size(50.dp, 50.dp)
                        .padding(1.dp),
                        painter = rememberGlidePainter(
                          MediaItemUtils.getAlbumArtUri(song = song),
                            fadeIn = true
                        ),
                        contentDescription = "")
                Column(verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(start = DEFAULT_PADDING)) {
                        Text(text = MediaItemUtils.getTitle(song),
                                style = MaterialTheme.typography.body2,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis)
                        Row(Modifier.fillMaxWidth()
                                .padding(0.dp, 0.dp, 5.dp, 0.dp)) {
                                Text(
                                        modifier = Modifier.weight(9f),
                                        text = MediaItemUtils.getArtist(song)!!,
                                        maxLines = 1,
                                        style = MaterialTheme.typography.caption,
                                        overflow = TextOverflow.Ellipsis)
                                Text(
                                        modifier = Modifier.weight(2f),
                                        text = formatTime(MediaItemUtils.getDuration(song)),
                                        maxLines = 1,
                                        style = MaterialTheme.typography.caption,
                                        textAlign = TextAlign.Right
                                )
                        }
                }
        }

}


