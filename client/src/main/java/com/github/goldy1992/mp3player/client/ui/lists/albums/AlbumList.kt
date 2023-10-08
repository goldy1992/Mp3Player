package com.github.goldy1992.mp3player.client.ui.lists.albums

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.media.Albums
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.ui.DEFAULT_WINDOW_CLASS_SIZE
import com.github.goldy1992.mp3player.client.ui.buttons.LoadingIndicator
import com.github.goldy1992.mp3player.client.ui.lists.NoPermissions
import com.github.goldy1992.mp3player.client.ui.lists.NoResultsFound
import com.github.goldy1992.mp3player.commons.MediaItemType

@Preview
@Composable
fun AlbumsList(modifier : Modifier = Modifier,
               windowSize: WindowSizeClass = DEFAULT_WINDOW_CLASS_SIZE,
               albums : Albums = Albums.NOT_LOADED,
               onAlbumSelected : (Album) -> Unit = {}) {
    when(albums.state) {
        State.LOADING -> LoadingIndicator()
        State.NO_RESULTS -> NoResultsFound(mediaItemType = MediaItemType.ALBUMS)
        State.LOADED -> AlbumListImpl(
                            modifier = modifier,
                            albums = albums.albums,
                            windowSize = windowSize,
                            onAlbumSelected = onAlbumSelected
                        )
        State.NO_PERMISSIONS -> NoPermissions()
        else -> Text(text = "Unknown album state")
    }
}

val testAlbumList = listOf(
    Album(id = "id1", title = "title1", artist = "artist1" ),
    Album(id = "id2", title = "title2", artist = "artist2" )
)
@Preview
@Composable
private fun AlbumListImpl(modifier : Modifier = Modifier,
                          windowSize: WindowSizeClass = DEFAULT_WINDOW_CLASS_SIZE,
                          albums : List<Album> = testAlbumList,
                          onAlbumSelected : (Album) -> Unit = {}
) {

    val numberOfColumns = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 2
        WindowWidthSizeClass.Medium -> 3
        WindowWidthSizeClass.Expanded -> 4
        else -> 2
    }
    LazyVerticalGrid(modifier = Modifier.padding(5.dp), columns = GridCells.Fixed(numberOfColumns)) {
        items(albums.size) {
            val currentAlbum = albums[it]
            Column(modifier = modifier){//modifier.padding(5.dp)) {
                AlbumListItem(album = currentAlbum,
                onClick = { onAlbumSelected(currentAlbum)})
            }

        }
    }
}