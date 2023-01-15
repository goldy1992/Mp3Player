package com.github.goldy1992.mp3player.client.ui.lists.albums

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import coil.annotation.ExperimentalCoilApi
import com.github.goldy1992.mp3player.client.ui.lists.songs.AlbumArt
import com.github.goldy1992.mp3player.commons.MediaItemUtils

@OptIn(ExperimentalCoilApi::class)
@Preview
@Composable
fun AlbumListItem(modifier: Modifier = Modifier,
                album : MediaItem = MediaItem.EMPTY,
                localDensity: Density = LocalDensity.current) {
    var width by remember { mutableStateOf(0.dp) }
    Card(modifier= modifier.fillMaxSize()
        .onSizeChanged { it -> width = (it.width.toFloat() / localDensity.density).dp },
        elevation = CardDefaults.outlinedCardElevation()) {
        Column(Modifier.fillMaxSize()) {
            AlbumArt(album, modifier = modifier.align(Alignment.CenterHorizontally).size(width))
            Text(MediaItemUtils.getAlbumTitle(album), style = MaterialTheme.typography.bodyMedium)
            Text(MediaItemUtils.getAlbumArtist(album), style = MaterialTheme.typography.bodySmall)
        }
    }
}