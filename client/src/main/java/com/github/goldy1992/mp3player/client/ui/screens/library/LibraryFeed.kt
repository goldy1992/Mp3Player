package com.github.goldy1992.mp3player.client.ui.screens.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.goldy1992.mp3player.client.models.media.Albums
import com.github.goldy1992.mp3player.client.ui.components.AlbumArtAsync
import com.github.goldy1992.mp3player.client.ui.components.feed.Feed
import com.github.goldy1992.mp3player.client.ui.components.feed.title

@Preview
@Composable
fun LibraryFeedNoSelection(albumsProvider: () -> Albums = { Albums.NOT_LOADED}) {
    val state = rememberLazyGridState()

    Feed(
        columns = GridCells.Fixed(3),
        state = state,
        contentPadding = PaddingValues(
            horizontal = 32.dp,
            vertical = 48.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        title(contentType = "feed-title") {
            Text(
                text = "Recently Played",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(PaddingValues(vertical = 24.dp))
            )
        }

        val albums = albumsProvider()

        items(albums.albums.size, key = {  albums.albums[it].id }) {
            val currentAlbum = albums.albums[it]
            AlbumArtAsync(modifier= Modifier.size(150.dp), uri = currentAlbum.artworkUri, contentDescription = "")
        }
    }
}