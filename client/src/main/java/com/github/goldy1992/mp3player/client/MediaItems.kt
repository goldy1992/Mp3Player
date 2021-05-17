package com.github.goldy1992.mp3player.client

import android.support.v4.media.MediaBrowserCompat.MediaItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MediaItems {

    var rootItems : LiveData<List<MediaItem>>? = MutableLiveData()

    val itemMap : HashMap<MediaItem, LiveData<List<MediaItem>>> = HashMap()
}