package com.github.goldy1992.mp3player.client.viewmodels

import android.support.v4.media.MediaBrowserCompat
import androidx.hilt.Assisted
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import androidx.hilt.lifecycle.ViewModelInject

class SongListViewModel

    @ViewModelInject
    constructor() : MediaListViewModel()

{
    override fun itemSelected(item: MediaBrowserCompat.MediaItem?) {
        TODO("Not yet implemented")
    }
}