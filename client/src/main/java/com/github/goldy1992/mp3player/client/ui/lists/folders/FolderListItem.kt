package com.github.goldy1992.mp3player.client.ui.lists.folders;

import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.DEFAULT_PADDING
import com.github.goldy1992.mp3player.client.ui.WindowSize
import com.github.goldy1992.mp3player.client.ui.getWindowSizeClass
import com.github.goldy1992.mp3player.commons.MediaItemUtils

@Preview
@Composable
fun FolderListItem(folder : MediaItem? = MediaItemUtils.getEmptyMediaItem(),
                   onClick: (selectedFolder : MediaItem?) -> Unit = {}) {
    Column {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.background)
                .padding(start = DEFAULT_PADDING, end = DEFAULT_PADDING),
            contentAlignment = Alignment.Center
        ) {

            when (getWindowSizeClass(DpSize(maxWidth, maxHeight))) {
                WindowSize.Expanded -> ExpandedFolderListItem(folder, onClick)
                WindowSize.Compact -> CompactFolderListItem(folder, onClick)
                else -> MediumFolderListItem(folder, onClick)

            }
        }
        Divider()
    }

}

@Preview
@Composable
fun CompactFolderListItem(folder : MediaItem? = MediaItemUtils.getEmptyMediaItem(),
                   onClick: (selectedFolder : MediaItem?) -> Unit = {}) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.background)
        .clickable(onClick = { onClick(folder) }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FolderIcon()
        Column(verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = DEFAULT_PADDING)) {
            Text(text = MediaItemUtils.getDirectoryName(folder),
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)

            Text(
                text = MediaItemUtils.getDirectoryPath(folder),
                maxLines = 1,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis)
        }
    }

}

@Preview
@Composable
fun MediumFolderListItem(folder : MediaItem? = MediaItemUtils.getEmptyMediaItem(),
                          onClick: (selectedFolder : MediaItem?) -> Unit = {}) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.background)
        .clickable(onClick = { onClick(folder) }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FolderIcon()
        Column(verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = DEFAULT_PADDING)) {
            Text(text = MediaItemUtils.getDirectoryName(folder),
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)

            Text(
                text = MediaItemUtils.getDirectoryPath(folder),
                maxLines = 1,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis)
        }
    }

}

@Preview
@Composable
fun ExpandedFolderListItem(folder : MediaItem? = MediaItemUtils.getEmptyMediaItem(),
                          onClick: (selectedFolder : MediaItem?) -> Unit = {}) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.background)
        .clickable(onClick = { onClick(folder) }),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FolderIcon()
        Box(
            modifier = Modifier.padding(start = DEFAULT_PADDING)
                .weight(3f)
        ) {
            Text(
                text = MediaItemUtils.getDirectoryName(folder),
                style = MaterialTheme.typography.body2,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Box(Modifier.weight(3f)) {
            Text(
                text = MediaItemUtils.getDirectoryPath(folder),
                maxLines = 1,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(Modifier.weight(1f),
        contentAlignment = Alignment.CenterEnd) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "")
            }
        }

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
            .size(60.dp)
    )
}

