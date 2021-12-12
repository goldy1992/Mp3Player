package com.github.goldy1992.mp3player.client.ui.lists.folders

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.DEFAULT_PADDING
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getEmptyMediaItem
import org.apache.commons.collections4.CollectionUtils.isEmpty
import org.apache.commons.collections4.CollectionUtils.isNotEmpty

@Composable
@Preview
fun FolderList(foldersData : LiveData<List<MediaBrowserCompat.MediaItem>> = MutableLiveData(emptyList()),
               onFolderSelected : (folder : MediaBrowserCompat.MediaItem?) -> Unit = {}) {
    val folders by foldersData.observeAsState()
    
    Card() {
        
    }

    when {
        folders == null -> LoadingIndicator()
        isEmpty(folders) -> EmptyFoldersList()
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                .fillMaxWidth()
                .background(MaterialTheme.colors.background),
            textAlign = TextAlign.Center)
    }
}

@Preview
@Composable
fun FolderListHeader(folder: MediaBrowserCompat.MediaItem? = getEmptyMediaItem()) {
    Row(
        modifier = Modifier
            .padding(DEFAULT_PADDING)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically) {

        val folderIconContentDescr = stringResource(id = R.string.folder_icon)
        Icon(
            Icons.Filled.Folder,
            contentDescription = folderIconContentDescr,
            modifier = Modifier
                .padding(2.dp)
                .size(75.dp)
        )

        Column(verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = MediaItemUtils.getDirectoryName(folder),
                style = MaterialTheme.typography.h6
            )
            Text(
                text = MediaItemUtils.getDirectoryPath(folder),
                style = MaterialTheme.typography.subtitle1
            )
            Text(text = "${MediaItemUtils.getFileCount(folder)} Items",
                style = MaterialTheme.typography.caption)
        }
    }

}