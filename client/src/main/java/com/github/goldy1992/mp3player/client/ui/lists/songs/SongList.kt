package com.github.goldy1992.mp3player.client.ui.lists.songs

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.MediaItem
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.ui.DEFAULT_PADDING
import org.apache.commons.collections4.CollectionUtils.isEmpty
import org.apache.commons.lang3.StringUtils

private const val logTag = "SongList"

@ExperimentalCoilApi
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongList(
    modifier : Modifier = Modifier,
    songs : List<MediaItem> = emptyList(),
    isPlayingProvider : () -> Boolean = {false},
    currentMediaItemProvider : () -> MediaItem = {MediaItem.EMPTY},
    onSongSelected : (itemIndex: Int, songs : List<MediaItem>) -> Unit = { _, _ -> }) {

    Log.i(logTag, "song list size: ${songs.size}")
    val isPlaying = isPlayingProvider()
    val currentMediaItem = currentMediaItemProvider()

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
                        Log.i(logTag, "isItemSelected: $isItemSelected isPlaying: ${isPlaying}")
                        val isItemPlaying = if (isPlaying) isItemSelected  else false
                        SongListItem(song = song, isSelected = isItemSelected, onClick =  {onSongSelected(itemIndex, songs) })
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
    //Log.i(logTag, "songId: ${song.mediaId}, currentItemId: ${currentItem.mediaId}")
    return StringUtils.equals(song.mediaId, currentItem.mediaId)
}