package com.github.goldy1992.mp3player.client.viewmodels

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import javax.inject.Inject

@FragmentScope
class SongListViewModel

    @Inject
    constructor(val mediaBrowserAdapter: MediaBrowserAdapter,
                val mediaControllerAdapter: MediaControllerAdapter,
                parentItemTypeId: String)
    : MediaListViewModel(mediaBrowserAdapter, parentItemTypeId)

{
    override fun itemSelected(item: MediaBrowserCompat.MediaItem?) {
        TODO("Not yet implemented")
    }
}