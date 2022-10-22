package com.github.goldy1992.mp3player.client.viewmodels

import android.util.Log
import androidx.concurrent.futures.await
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.flows.mediabrowser.OnChildrenChangedFlow
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.data.flows.player.MetadataFlow
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
        private val isPlayingFlow: IsPlayingFlow,
        private val metadataFlow: MetadataFlow,
        private val onChildrenChangedFlow: OnChildrenChangedFlow,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher
        ) : LogTagger, ViewModel() {

    private val _rootItems : MutableStateFlow<List<MediaItem>> = MutableStateFlow(emptyList())
    val rootItems : StateFlow<List<MediaItem>> = _rootItems


    private val _rootItemMap = HashMap<String, MutableStateFlow<List<MediaItem>>>()
    val rootItemMap = HashMap<String, StateFlow<List<MediaItem>>>()

    var currentNavigationItem : MutableLiveData<MediaItemType> = MutableLiveData(MediaItemType.SONGS)

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
            onChildrenChangedFlow.flow.filter {
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


    private val mediaControllerAsync : ListenableFuture<MediaController> = mediaControllerAdapter.mediaControllerFuture

    // isPlaying
    private val _isPlayingState = MutableStateFlow(false)
    val isPlaying : StateFlow<Boolean> = _isPlayingState

    init {
        viewModelScope.launch(mainDispatcher) {
            _isPlayingState.value = mediaControllerAsync.await().isPlaying
        }
        viewModelScope.launch {
            isPlayingFlow.flow().collect {
                _isPlayingState.value = it
            }
        }
    }


    // metadata
    private val _metadataState = MutableStateFlow(MediaMetadata.EMPTY)
    val metadata : StateFlow<MediaMetadata> = _metadataState

    init {
        viewModelScope.launch(mainDispatcher) {
            _metadataState.value = mediaControllerAsync.await().mediaMetadata
        }
        viewModelScope.launch {
            metadataFlow.flow().collect {
                _metadataState.value = it
            }
        }
    }

    override fun logTag(): String {
        return "LibScrnViewModel"
    }
}