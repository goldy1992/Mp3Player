package com.github.goldy1992.mp3player.client.ui

import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.viewmodels.MediaRepository
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import org.apache.commons.collections4.CollectionUtils.isEmpty

@Composable
fun FolderList(mediaRepository: MediaRepository, mediaController: MediaControllerAdapter) {
    val foldersData = mediaRepository.itemMap[MediaItemType.FOLDERS]
    if (foldersData == null) {
        EmptyFoldersList()
    } else {
        val foldersState : State<List<MediaBrowserCompat.MediaItem>?> = foldersData.observeAsState()
        val folders = foldersState.value

        if (isEmpty(folders)) {
            EmptyFoldersList()
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(count = folders!!.size) { itemIndex ->
                    run {
                        val song = folders[itemIndex]
                        FolderListItem(song) {
                            val libraryId = MediaItemUtils.getLibraryId(song)
                            Log.i("ON_CLICK_Folder", "clicked folder with id : $libraryId")
                            //mediaController.playFromMediaId(libraryId, null)
                            // TODO: Implement go to folder screen
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
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "No folders containing audio tracks found on your device.",
            textAlign = TextAlign.Center)
    }
}