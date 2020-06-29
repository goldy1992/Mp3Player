package com.github.goldy1992.mp3player.client.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import com.github.goldy1992.mp3player.commons.MediaItemType
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class MediaListViewModelFactory

    @Inject
    constructor(
            val mediaBrowserAdapter: MediaBrowserAdapter,
            val mediaControllerAdapter: MediaControllerAdapter,
            var parentItemType: MediaItemType,
                var parentItemTypeId: String)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return  SongListViewModel(mediaBrowserAdapter, mediaControllerAdapter, parentItemTypeId) as T//MediaListViewModel(mediaBrowserAdapter, mediaControllerAdapter,parentItemTypeId, parentItemType) as T
    }
}