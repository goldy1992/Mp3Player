package com.github.goldy1992.mp3player.client.ui

import android.support.v4.media.MediaBrowserCompat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.Screen
import org.apache.commons.collections4.CollectionUtils.isEmpty

@Composable
fun FolderList(foldersData : LiveData<List<MediaBrowserCompat.MediaItem>>,
                navController: NavController,
                mediaRepository: MediaRepository) {
    val folders by foldersData.observeAsState()

    when {
        folders == null -> LoadingIndicator()
        isEmpty(folders) -> EmptyFoldersList()
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(count = folders!!.size) { itemIndex ->
                    run {
                        val folder = folders!![itemIndex]

                        FolderListItem(folder) {
                            mediaRepository.currentFolder = folder
                            navController.navigate(Screen.FOLDER.name)
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
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center)
    }
}