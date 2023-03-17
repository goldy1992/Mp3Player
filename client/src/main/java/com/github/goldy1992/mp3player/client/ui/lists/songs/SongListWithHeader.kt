package com.github.goldy1992.mp3player.client.ui.lists.songs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.data.Songs
import com.github.goldy1992.mp3player.client.utils.SongUtils

@ExperimentalCoilApi
@Composable
@OptIn(ExperimentalFoundationApi::class)
fun LoadedSongsListWithHeader(
    songs: Songs,
    modifier: Modifier = Modifier,
    headerItem: @Composable (() -> Unit) = {},
    currentSong: Song,
    isPlayingProvider: () -> Boolean,
    onSongSelected: (itemIndex: Int, songs: Songs) -> Unit
) {
    val songList = songs.songs
    val songsListDescr = stringResource(id = R.string.songs_list)
    val header: @Composable () -> Unit = headerItem ?: {}
    val itemCount = songList.size + 1
    LazyColumn(
        modifier = modifier.semantics {
            contentDescription = songsListDescr
        }) {
        items(count = itemCount,
            key = {
                if (it > 0) {
                    songList[it - 1].id
                } else {
                    "header"
                }

            }
        ) { itemIndex ->
            if (itemIndex > 0) {
                val song = songList[itemIndex - 1]
                val isItemSelected = SongUtils.isSongItemSelected(song, currentSong)
                SongListItem(
                    song = song,
                    isSelected = isItemSelected,
                    isPlayingProvider = isPlayingProvider,
                    onClick = { onSongSelected(itemIndex, songs) })
            } else {
                header()
            }
        }
    }
}
