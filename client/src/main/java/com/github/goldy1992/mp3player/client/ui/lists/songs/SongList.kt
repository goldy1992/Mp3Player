package com.github.goldy1992.mp3player.client.ui.lists.songs

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.DEFAULT_PADDING
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import com.github.goldy1992.mp3player.commons.MetadataUtils
import org.apache.commons.collections4.CollectionUtils.isEmpty
import org.apache.commons.lang3.StringUtils

@ExperimentalCoilApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongList(
    modifier : Modifier = Modifier,
    songs : List<MediaBrowserCompat.MediaItem> = emptyList(),
    mediaControllerAdapter: MediaControllerAdapter,
    onSongSelected : (song : MediaBrowserCompat.MediaItem) -> Unit = {}) {

    val isPlaying by mediaControllerAdapter.isPlaying.observeAsState()
    val metadata by mediaControllerAdapter.metadata.observeAsState()

    when {
        isEmpty(songs) -> EmptySongsList()
        else -> {
            val songsListDescr = stringResource(id = R.string.songs_list)
            LazyColumn(
                modifier = modifier.semantics {
                    contentDescription = songsListDescr
                }) {
                items(count = songs.size) { itemIndex ->
                    run {
                        val song = songs[itemIndex]
                        val isItemSelected = isItemSelected(song, metadata)
                        val isItemPlaying = if (isPlaying == true) isItemSelected  else false
                        SongListItem(song = song, isPlaying = isItemPlaying, mediaController = mediaControllerAdapter, isSelected = isItemSelected, onClick = onSongSelected)
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

private fun isItemSelected(song : MediaBrowserCompat.MediaItem?, metadata: MediaMetadataCompat?) : Boolean {
    return if (song != null && metadata != null ) {
        val metaDataMediaId = MetadataUtils.getMediaId(metadata)
        val songMediaId = MediaItemUtils.getMediaId(song)
        StringUtils.equals(songMediaId, metaDataMediaId)
    } else false
}