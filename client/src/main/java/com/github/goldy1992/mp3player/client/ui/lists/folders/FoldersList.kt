package com.github.goldy1992.mp3player.client.ui.lists.folders

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.DEFAULT_PADDING
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import org.apache.commons.collections4.CollectionUtils.isEmpty
import org.apache.commons.collections4.CollectionUtils.isNotEmpty

@Composable
@Preview
fun FolderList(foldersData : LiveData<List<MediaBrowserCompat.MediaItem>> = MutableLiveData(emptyList()),
               onFolderSelected : (folder : MediaBrowserCompat.MediaItem?) -> Unit = {}) {
    val folders by foldersData.observeAsState()

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
            modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colors.background),
            textAlign = TextAlign.Center)
    }
}