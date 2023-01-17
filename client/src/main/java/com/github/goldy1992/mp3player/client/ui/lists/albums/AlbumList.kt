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
import com.github.goldy1992.mp3player.client.data.Album
import com.github.goldy1992.mp3player.client.data.Albums
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.client.ui.lists.NoResultsFound
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.MediaItemType

@Composable
fun AlbumsList(albums : Albums) {
    when(albums.state) {
        State.LOADING -> LoadingIndicator()
        State.NO_RESULTS -> NoResultsFound(mediaItemType = MediaItemType.ALBUMS)
        State.LOADED -> AlbumListImpl(albums = albums.albums)
        else -> Text(text = "Unknown album state")
    }
}

val testAlbumList = listOf(
    Album(id = "id1", albumTitle = "title1", albumArtist = "artist1" ),
    Album(id = "id2", albumTitle = "title2", albumArtist = "artist2" )
)
@Preview
@Composable
private fun AlbumListImpl(modifier : Modifier = Modifier,
                          albums : List<Album> = testAlbumList,
) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(albums.size) {
            Column(modifier.padding(5.dp)) {
                AlbumListItem(album = albums[it])
            }

        }
    }
}