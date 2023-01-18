package com.github.goldy1992.mp3player.client.ui.lists.folders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.Folder
import com.github.goldy1992.mp3player.client.data.Folders
import com.github.goldy1992.mp3player.client.ui.DEFAULT_PADDING
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.client.ui.lists.NoResultsFound
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.MediaItemType
import org.apache.commons.collections4.CollectionUtils.isNotEmpty

@Composable
@Preview
fun FolderList(folders : Folders = Folders(State.NOT_LOADED),
               onFolderSelected : (folder : Folder) -> Unit = {}) {

    when (folders.state) {
        State.LOADING -> LoadingIndicator()
        State.NO_RESULTS -> NoResultsFound(mediaItemType = MediaItemType.FOLDERS)
        State.LOADED -> {
            val folderListDescr = stringResource(id = R.string.folder_list)

            LazyColumn(
                modifier = Modifier.semantics { contentDescription = folderListDescr }
            ) {
                val folderItems = folders.folders
                if (isNotEmpty(folderItems)) {
                    items(count = folderItems.size) { itemIndex ->
                        run {
                            val folder = folderItems[itemIndex]
                            FolderListItem(folder, onFolderSelected)
                        }
                    }
                }
            }
        }
        else -> {}
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
