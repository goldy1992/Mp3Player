package com.github.goldy1992.mp3player.client.viewmodels

import androidx.lifecycle.LiveData
import androidx.media3.common.MediaItem

data class MediaRepository

    constructor(
        val rootItems : LiveData<List<MediaItem>>)

{
    var currentFolder : MediaItem? = null

}