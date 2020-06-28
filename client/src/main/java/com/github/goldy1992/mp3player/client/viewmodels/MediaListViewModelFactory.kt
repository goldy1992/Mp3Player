package com.github.goldy1992.mp3player.client.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import com.github.goldy1992.mp3player.commons.MediaItemType
import javax.inject.Inject

@FragmentScope
class MediaListViewModelFactory

    @Inject
    constructor(
            val mediaBrowserAdapter: MediaBrowserAdapter,
            var parentItemType: MediaItemType,
                var parentItemTypeId: String)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return null// MediaListViewModel(mediaBrowserAdapter, parentItemType, parentItemTypeId) as T
    }
}