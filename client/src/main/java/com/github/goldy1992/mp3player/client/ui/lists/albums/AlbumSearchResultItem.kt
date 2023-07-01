package com.github.goldy1992.mp3player.client.ui.lists.albums

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.data.Album
import com.github.goldy1992.mp3player.client.ui.components.AlbumArtAsync

@OptIn(
    ExperimentalFoundationApi::class
)
@Preview
@Composable
fun AlbumSearchResultItem(album : Album = Album(),
                   containerColor : Color = MaterialTheme.colorScheme.surface,
                   onClick: (selectedAlbum : Album) -> Unit = {}) {
    ListItem(
        modifier = Modifier.combinedClickable(
            onClick = { onClick(album) },
            onLongClick = { }
        ),
        colors = ListItemDefaults.colors(containerColor = containerColor),
        leadingContent = { AlbumArtAsync(uri = album.albumArt, contentDescription = album.albumTitle, modifier = Modifier.size(40.dp))},
        headlineContent = {
                Text(text = album.albumTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
            },
        supportingContent = {
            Text(
                text = album.albumArtist,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis)
        },
        trailingContent = {
            IconButton(onClick = { onClick(album) }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}
