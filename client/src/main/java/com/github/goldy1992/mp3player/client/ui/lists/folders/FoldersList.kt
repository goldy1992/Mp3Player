package com.github.goldy1992.mp3player.client.ui.lists.folders

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.DEFAULT_PADDING
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getEmptyMediaItem
import org.apache.commons.collections4.CollectionUtils.isEmpty
import org.apache.commons.collections4.CollectionUtils.isNotEmpty

@Composable
@Preview
fun FolderList(folders : List<MediaBrowserCompat.MediaItem> = emptyList(),
               onFolderSelected : (folder : MediaBrowserCompat.MediaItem?) -> Unit = {}) {

    when {
        folders == null -> LoadingIndicator()
        isEmpty(folders) -> EmptyFoldersList()
        else -> {
            val folderListDescr = stringResource(id = R.string.folder_list)

            LazyColumn(Modifier.semantics {
                contentDescription = folderListDescr
            }) {
                if (isNotEmpty(folders)) {
                    items(count = folders!!.size) { itemIndex ->
                        run {
                            val folder = folders!![itemIndex]
                            FolderListItem(folder, onFolderSelected)
                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
fun EmptyFoldersList() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(DEFAULT_PADDING)) {
        Text(text = stringResource(id = R.string.no_folders_with_songs),
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium)
    }
}
