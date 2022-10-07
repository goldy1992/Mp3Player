package com.github.goldy1992.mp3player.client.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.AsyncPlayerListener
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.MediaItemType
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

    private val _rootItems : MutableStateFlow<List<MediaItem>> = MutableStateFlow(emptyList())
    val rootItems : StateFlow<List<MediaItem>> = _rootItems


    private val _rootItemMap = HashMap<String, MutableStateFlow<List<MediaItem>>>()
    val rootItemMap = HashMap<String, StateFlow<List<MediaItem>>>()

    var currentNavigationItem : MutableLiveData<MediaItemType> = MutableLiveData(MediaItemType.SONGS)


    //        var mediaItemSelected : MutableLiveData<MediaBrowserCompat.MediaItem> = MutableLiveData(
//            MediaItemUtils.getEmptyMediaItem()
//        )
//
//        var mediaItemChildren : LiveData<List<MediaBrowserCompat.MediaItem>> = MutableLiveData(emptyList())

    init {
        viewModelScope.launch {
            val rootItem = mediaBrowserAdapter.getLibraryRoot()
            val rootItemId = rootItem.mediaId
            mediaBrowserAdapter.subscribe(rootItemId)
            val rootChildren = mediaBrowserAdapter.getChildren(rootItemId)
            _rootItems.value = rootChildren

            for (mediaItem : MediaItem in rootChildren) {
                val mediaItemId = mediaItem.mediaId
                mediaBrowserAdapter.subscribe(mediaItemId)
                val children = mediaBrowserAdapter.getChildren(mediaItemId)
                _rootItemMap[mediaItemId] = MutableStateFlow(children)
                rootItemMap[mediaItemId] = _rootItemMap[mediaItemId]!!
            }


        }

    }
}