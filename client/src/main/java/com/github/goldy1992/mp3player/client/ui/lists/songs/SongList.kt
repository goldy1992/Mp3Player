package com.github.goldy1992.mp3player.client.ui.lists.songs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.MediaItem
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.AsyncPlayerListener
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.DEFAULT_PADDING
import org.apache.commons.collections4.CollectionUtils.isEmpty
import org.apache.commons.lang3.StringUtils

@ExperimentalCoilApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongList(
    modifier : Modifier = Modifier,
    songs : List<MediaItem> = emptyList(),
    mediaControllerAdapter: MediaControllerAdapter,
    asyncPlayerListener: AsyncPlayerListener,
    onSongSelected : (song : MediaItem) -> Unit = {}) {

    val metadata by asyncPlayerListener.mediaMetadataState.collectAsState()
    val isPlaying by asyncPlayerListener.isPlayingState.collectAsState()
    val currentMediaItem = remember (isPlaying, metadata) {
        mediaControllerAdapter.getCurrentMediaItem()
    }

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
                        val isItemSelected = isItemSelected(song, currentMediaItem)
                        val isItemPlaying = if (isPlaying == true) isItemSelected  else false
                        SongListItem(song = song, isPlaying = isItemPlaying, isSelected = isItemSelected, onClick = onSongSelected)
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

private fun isItemSelected(song : MediaItem, currentItem : MediaItem) : Boolean {
    return StringUtils.equals(song.mediaId, currentItem.mediaId)
}