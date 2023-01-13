package com.github.goldy1992.mp3player.client.ui.lists.albums

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.media3.common.MediaItem
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.ui.lists.songs.AlbumArt

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
fun AlbumListItem(modifier: Modifier = Modifier,
                album : MediaItem = MediaItem.EMPTY) {
    Card(modifier.fillMaxSize().background(Color.Yellow)) {
        Column(Modifier.fillMaxSize()) {
            AlbumArt(album, modifier = Modifier.align(Alignment.CenterHorizontally))
            Text("Album Name")
            Text("Album Artist")
        }
    }
}