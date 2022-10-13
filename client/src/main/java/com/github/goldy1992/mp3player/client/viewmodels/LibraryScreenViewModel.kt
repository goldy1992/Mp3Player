package com.github.goldy1992.mp3player.client.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.data.flows.player.MetadataFlow
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryScreenViewModel
    @Inject
    constructor(
        val mediaBrowserAdapter: MediaBrowserAdapter,
        val mediaControllerAdapter: MediaControllerAdapter,
        val metadataFlow: MetadataFlow,
        val isPlayingFlow: IsPlayingFlow) : LogTagger, ViewModel() {

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

    var rootItem : MediaItem? = null
    var rootItemId : String? = null
    init {
        viewModelScope.launch {
            val collectedRootItem = mediaBrowserAdapter.getLibraryRoot()
            rootItem = collectedRootItem
            rootItemId = collectedRootItem.mediaId
            mediaBrowserAdapter.subscribe(collectedRootItem.mediaId)
        }

        viewModelScope.launch {
            mediaBrowserAdapter.onChildrenChangedFlow.filter {
                Log.i(logTag(), "filtering: id: ${it.parentId}")
                it.parentId == rootItemId || rootItemMap.containsKey(it.parentId)
            }.collect {

                if (it.parentId == rootItemId) {
                    val rootChildren = mediaBrowserAdapter.getChildren(it.parentId, 0, it.itemCount)
                    _rootItems.value = rootChildren
                    for (mediaItem : MediaItem in rootChildren) {
                        val mediaItemId = mediaItem.mediaId
                        mediaBrowserAdapter.subscribe(mediaItemId)
                        _rootItemMap[mediaItemId] = MutableStateFlow(emptyList())
                        rootItemMap[mediaItemId] = _rootItemMap[mediaItemId]!!
                    }
                } else if (rootItemMap.containsKey(it.parentId)) {
                    val children = mediaBrowserAdapter.getChildren(it.parentId, 0, it.itemCount)
                    _rootItemMap[it.parentId]?.value = children
                }
            }
        }

    }

    override fun logTag(): String {
        return "LibScrnViewModel"
    }
}