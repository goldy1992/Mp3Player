package com.github.goldy1992.mp3player.client.ui.lists.albums

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.client.ui.lists.NoResultsFound
import com.github.goldy1992.mp3player.client.ui.states.LibraryResultState
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import com.github.goldy1992.mp3player.commons.MediaItemType

@Composable
fun AlbumsList(albums : LibraryResultState) {
    when(albums.state) {
        State.LOADING -> LoadingIndicator()
        State.NO_RESULTS -> NoResultsFound(mediaItemType = MediaItemType.ALBUMS)
        State.LOADED -> AlbumListImpl(albums.results)
        else -> Text(text = "Unknown album state")
    }
}

val testAlbuList = listOf(
    MediaItemBuilder("").setAlbumTitle("title1").setAlbumArtist("artist1").build(),
    MediaItemBuilder("").setAlbumTitle("title2").setAlbumArtist("artist2").build())
@Preview
@Composable
private fun AlbumListImpl(albums : List<MediaItem> = testAlbuList,
                            modifier : Modifier = Modifier) {

    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(albums.size) {
            Column(Modifier.padding(5.dp)) {
                AlbumListItem(album = albums[it])
            }

        }
    }
}