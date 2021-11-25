package com.github.goldy1992.mp3player.client.ui

import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LiveData
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import org.apache.commons.collections4.CollectionUtils.isEmpty

@ExperimentalCoilApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongList(songsData : LiveData<List<MediaBrowserCompat.MediaItem>>,
             mediaController: MediaControllerAdapter) {

    val songs by songsData.observeAsState()
    when {
        songs == null -> LoadingIndicator()
        isEmpty(songs) -> EmptySongsList()
        else -> {
            val songsListDescr = stringResource(id = R.string.songs_list)
            LazyColumn(modifier = Modifier
                    .fillMaxSize()
                .background(Color.Red)
                    .semantics {
                        contentDescription = songsListDescr
                    }) {
                items(count = songs!!.size) { itemIndex ->
                    run {
                        val song = songs!![itemIndex]
                        SongListItem(song) {
                            val libraryId = MediaItemUtils.getLibraryId(song)
                            Log.i("ON_CLICK_SONG", "clicked song with id : $libraryId")
                            mediaController.playFromMediaId(libraryId, null)
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun EmptySongsList() {

    Column(modifier = Modifier
            .fillMaxSize()
            .padding(DEFAULT_PADDING)) {
        Text(text = stringResource(id = R.string.no_songs_on_device),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth())

    }
}