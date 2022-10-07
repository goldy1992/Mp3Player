package com.github.goldy1992.mp3player.client.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.AsyncPlayerListener
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MetaDataKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryScreenViewModel
    @Inject
    constructor(
        val mediaBrowserAdapter: MediaBrowserAdapter,
        val mediaControllerAdapter: MediaControllerAdapter,
        val asyncPlayerListener: AsyncPlayerListener) : ViewModel() {

    init {
        viewModelScope.launch {
            val rootItem = mediaBrowserAdapter.getLibraryRoot()
            val rootItemId = rootItem.mediaId
            mediaBrowserAdapter.subscribe(rootItemId)
            _root.value = mediaBrowserAdapter.getChildren(rootItemId)
        }

    }

    val _root : MutableStateFlow<List<MediaItem>> = MutableStateFlow(emptyList())
    val rootItems : StateFlow<List<MediaItem>> = _root
        var currentNavigationItem : MutableLiveData<MediaItemType> = MutableLiveData(MediaItemType.SONGS)

//        var mediaItemSelected : MutableLiveData<MediaBrowserCompat.MediaItem> = MutableLiveData(
//            MediaItemUtils.getEmptyMediaItem()
//        )
//
//        var mediaItemChildren : LiveData<List<MediaBrowserCompat.MediaItem>> = MutableLiveData(emptyList())

    }