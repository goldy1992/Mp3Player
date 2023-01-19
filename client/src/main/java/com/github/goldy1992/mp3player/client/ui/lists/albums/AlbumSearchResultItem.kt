package com.github.goldy1992.mp3player.client.ui.lists.albums;

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.Album
import com.github.goldy1992.mp3player.client.ui.lists.songs.AlbumArt

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
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
        leadingContent = { AlbumArt(uri = album.albumArt, modifier = Modifier.size(40.dp))},
        headlineText = {
                Text(text = album.albumTitle,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
            },
        supportingText = {
            Text(
                text = album.albumArtist,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis)
        },
        trailingContent = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

@Composable
private fun FolderIcon() {
    val folderIconContentDescr = stringResource(id = R.string.folder_icon)
    Icon(
        Icons.Filled.Folder,
        contentDescription = folderIconContentDescr,
        modifier = Modifier
            .padding(2.dp)
            .size(40.dp),
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

