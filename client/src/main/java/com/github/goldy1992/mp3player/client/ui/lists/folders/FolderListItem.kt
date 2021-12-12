package com.github.goldy1992.mp3player.client.ui.lists.folders;

import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.MediaItemUtils

@Composable
fun FolderListItem(folder : MediaItem? = MediaItemUtils.getEmptyMediaItem(),
                   onClick: (selectedFolder : MediaItem?) -> Unit = {}) {
    Row(modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .clickable(onClick = {onClick(folder)}),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val folderIconContentDescr = stringResource(id = R.string.folder_icon)
        Icon(
            Icons.Filled.Folder,
            contentDescription = folderIconContentDescr
        )
        Column(verticalArrangement = Arrangement.Center) {
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

