package com.github.goldy1992.mp3player.client.viewmodels

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class SongListViewModel

    @Inject
    constructor(val mediaBrowserAdapter: MediaBrowserAdapter,
                 mediaControllerAdapter: MediaControllerAdapter,
                parentItemTypeId: String)
    : MediaListViewModel(mediaBrowserAdapter, mediaControllerAdapter,  parentItemTypeId)

{
    override fun itemSelected(item: MediaBrowserCompat.MediaItem?) {
        TODO("Not yet implemented")
    }
}