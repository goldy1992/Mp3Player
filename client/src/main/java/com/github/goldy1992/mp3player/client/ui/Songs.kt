package com.github.goldy1992.mp3player.client.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
fun SongList(mediaRepository: MediaRepository, mediaController: MediaControllerAdapter) {
    val songsData = mediaRepository.itemMap[MediaItemType.SONGS]

    if (songsData == null || isEmpty(songsData.value)) {
        EmptySongsList()
    } else {
        val songs by songsData!!.observeAsState()
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(count = songs.size) {
                    itemIndex ->
                run {
                    val song = songs[itemIndex]
                    songListItem(song) {
                        val libraryId = MediaItemUtils.getLibraryId(song)
                        Log.i("ON_CLICK_SONG", "clicked song with id : $libraryId")
                        mediaController.playFromMediaId(libraryId, null)
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun EmptySongsList() {

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "No audio tracks found on your device.",
        textAlign = TextAlign.Center)

    }
}