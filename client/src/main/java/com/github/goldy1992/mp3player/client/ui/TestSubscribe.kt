package com.github.goldy1992.mp3player.client.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter

@Composable
fun TestSubscribe(mediaBrowser: MediaBrowserAdapter) {

    var subscription : List<MediaItem> = remember {
        emptyList()
    }
    LaunchedEffect(Unit) {
        //subscription = mediaBrowser.subscribe("")
    }
}