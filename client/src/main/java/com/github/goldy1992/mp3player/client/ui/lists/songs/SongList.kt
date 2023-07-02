package com.github.goldy1992.mp3player.client.ui.lists.songs

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.Playlist
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.ui.DEFAULT_PADDING
import com.github.goldy1992.mp3player.client.ui.lists.NoPermissions
import com.github.goldy1992.mp3player.client.ui.lists.NoResultsFound
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.client.utils.SongUtils.isSongItemSelected
import com.github.goldy1992.mp3player.commons.MediaItemType

private const val LOG_TAG = "SongList"

@ExperimentalCoilApi
@Composable
fun SongList(
    modifier : Modifier = Modifier,
    playlist : Playlist = Playlist(State.NOT_LOADED),
    isPlayingProvider : () -> Boolean = {false},
    currentSongProvider : () -> Song = { Song() },
    onSongSelected : (itemIndex: Int, playlist : Playlist) -> Unit = { _, _ -> }) {
    Log.v(LOG_TAG, "SongList() invoked with ${playlist.songs.size} songs")
    val currentMediaItem = currentSongProvider()

    when (playlist.state) {
        State.NO_RESULTS -> NoResultsFound(mediaItemType = MediaItemType.SONGS)
        State.LOADED -> {
            LoadedSongsList(
                playlist,
                modifier,
                currentMediaItem,
                isPlayingProvider,
                onSongSelected
            )
        }
        State.LOADING -> {
            LoadingSongsList()
        }
        State.NO_PERMISSIONS -> {
            NoPermissions()
        }
        State.NOT_LOADED -> {

        }
        //else -> EmptySongsList()
    }
}

@ExperimentalCoilApi
@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun LoadedSongsList(
    playlist: Playlist,
    modifier: Modifier,
    currentMediaItem: Song,
    isPlayingProvider: () -> Boolean,
    onSongSelected: (itemIndex: Int, playlist: Playlist) -> Unit
) {
    val songList = playlist.songs
    val songsListDescr = stringResource(id = R.string.songs_list)
    val itemCount = songList.size
    LazyColumn(
        modifier = modifier.semantics {
            contentDescription = songsListDescr
        }) {
        items(
            count = itemCount,
            key = {songList[it].id }
        ) { itemIndex ->
            val song = songList[itemIndex]
            val isItemSelected = isSongItemSelected(song, currentMediaItem)
            SongListItem(
                song = song,
                isSelected = isItemSelected,
                onClick = { onSongSelected(itemIndex, playlist) })
        }

    }
}

@Preview
@Composable
fun EmptySongsList() {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(DEFAULT_PADDING)) {
        LazyColumn {
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.no_songs_on_device),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun LoadingSongsList() {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally)    {

        ListItem(
            colors = ListItemDefaults.colors(MaterialTheme.colorScheme.surface),
            trailingContent = {
                 CircularProgressIndicator()
            },
            headlineContent = {
                Text(
                    text = stringResource(id = R.string.loading),
                )
            },
        )
    }
}
