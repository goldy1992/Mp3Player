package com.github.goldy1992.mp3player.client.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.AsyncPlayerListener
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.MediaItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryScreenViewModel
    @Inject
    constructor(
        val mediaBrowserAdapter: MediaBrowserAdapter,
        val mediaControllerAdapter: MediaControllerAdapter,
        val asyncPlayerListener: AsyncPlayerListener) : ViewModel() {

        var currentNavigationItem : MutableLiveData<MediaItemType> = MutableLiveData(MediaItemType.SONGS)

//        var mediaItemSelected : MutableLiveData<MediaBrowserCompat.MediaItem> = MutableLiveData(
//            MediaItemUtils.getEmptyMediaItem()
//        )
//
//        var mediaItemChildren : LiveData<List<MediaBrowserCompat.MediaItem>> = MutableLiveData(emptyList())

    }