package com.github.goldy1992.mp3player.client.ui.lists.albums

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.ui.components.AlbumArtAsync

private const val LOG_TAG = "AlbumListItem"

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun AlbumListItem(modifier: Modifier = Modifier,
                  album : Album = Album(),
                  localDensity: Density = LocalDensity.current,
                  onClick : () -> Unit = {}) {
    var width by remember { mutableStateOf(0.dp) }
    Card(
        modifier= modifier
            .fillMaxSize()
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = {}
            )
            .onSizeChanged {
                Log.d(LOG_TAG, "onSizeChanged: $it")
                width = (it.width.toFloat() / localDensity.density).dp },
        elevation = CardDefaults.outlinedCardElevation()) {
        Column(Modifier.fillMaxSize()) {
            AlbumArtAsync(album.artworkUri,
                contentDescription = album.title,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width))
            Column(Modifier.padding(start = 4.dp, top = 4.dp, bottom = 4.dp, end=2.dp)) {
                Text(album.title,
                    modifier = Modifier.basicMarquee(),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines=1,
                    overflow = TextOverflow.Ellipsis)
                Text(album.artist,
                    modifier = Modifier.basicMarquee(),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines=1,
                    overflow = TextOverflow.Ellipsis)
            }
        }
    }
}