package com.github.goldy1992.mp3player.client.ui.lists.folders;

import androidx.media3.common.MediaItem
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.MediaItemUtils

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Preview
@Composable
fun FolderListItem(folder : MediaItem? = MediaItemUtils.getEmptyMediaItem(),
                   onClick: (selectedFolder : MediaItem?) -> Unit = {}) {
    ListItem(
        modifier = Modifier.combinedClickable(
            onClick = { onClick(folder) },
            onLongClick = { }
        ),
        icon = { FolderIcon()},
        secondaryText = {

            Text(
                text = MediaItemUtils.getDirectoryPath(folder),
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall,
                overflow = TextOverflow.Ellipsis)
        },
        trailing = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    ) {
        Text(text = MediaItemUtils.getDirectoryName(folder),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis)
    }
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

