package com.github.goldy1992.mp3player.client.viewmodels

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.LiveData
import com.github.goldy1992.mp3player.commons.MediaItemType

data class MediaRepository

    constructor(
        val rootItems : LiveData<List<MediaBrowserCompat.MediaItem>>)

{
    val itemMap : HashMap<MediaItemType, LiveData<List<MediaBrowserCompat.MediaItem>>?> = HashMap()

    var currentFolder : MediaBrowserCompat.MediaItem? = null

}