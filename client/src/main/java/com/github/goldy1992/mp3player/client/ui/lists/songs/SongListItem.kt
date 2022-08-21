package com.github.goldy1992.mp3player.client.ui.lists.songs

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.ui.components.Equalizer
import com.github.goldy1992.mp3player.client.utils.TimerUtils.formatTime
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getEmptyMediaItem

@OptIn(ExperimentalMaterialApi::class)
@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun SongListItem(song : MediaItem = getEmptyMediaItem(),
                 isPlaying : Boolean = false,
                 isSelected : Boolean = false,
                 mediaController: MediaControllerAdapter? = null,
                 onClick: (item : MediaItem) -> Unit = {}) {

    ListItem(
            modifier = Modifier
                .combinedClickable(
                    onClick = { onClick(song) },
                    onLongClick = { }
                )
                .requiredHeight(72.dp)
                .background(if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface),
            icon = {
                val albumArtUri = MediaItemUtils.getAlbumArtUri(song = song)
                AlbumArt(
                    uri = albumArtUri,
                    mediaController = mediaController,
                    isPlaying = isPlaying)
            },
            secondaryText = {
                Text(
                    text = MediaItemUtils.getArtist(song)!!,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                    overflow = TextOverflow.Ellipsis
                )

            },
            trailing = {
                Text(
                    modifier = Modifier.padding(start = 10.dp, top = 10.dp),
                    text = formatTime(MediaItemUtils.getDuration(song)),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        ) {
            Text(
                text = MediaItemUtils.getTitle(song),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    Divider(startIndent = 72.dp, color = MaterialTheme.colorScheme.surfaceVariant)
}

@ExperimentalCoilApi
@Preview
@Composable
private fun AlbumArt(uri : Uri? = null,
                     mediaController: MediaControllerAdapter? = null,
                    isPlaying: Boolean = true) {

    Surface(Modifier.size(40.dp)) {
        if (uri == null) {
            Image(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Filled.Help,
                contentDescription = ""
            )
        } else {
            Image(
                modifier = Modifier
                    .size(40.dp),
                painter = rememberImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(uri).build()
                ),
                contentDescription = ""
            )
        }

        if (isPlaying) {

            val list1: ArrayList<Float> = arrayListOf()
//            mediaController?.audioStream?.value?.frequencyMap?.forEachIndexed {
//                    indx, v ->
//                val height by animateFloatAsState(targetValue = v,
//                    animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing)
//                )
//                list1.add(height)
//            }

            Equalizer(modifier = Modifier.size(40.dp),
            bars = list1)
        }
    }
}


