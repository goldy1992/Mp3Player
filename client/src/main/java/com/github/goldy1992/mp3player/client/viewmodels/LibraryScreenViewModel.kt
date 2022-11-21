package com.github.goldy1992.mp3player.client.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.flows.mediabrowser.OnChildrenChangedFlow
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.data.flows.player.MetadataFlow
import com.github.goldy1992.mp3player.client.viewmodels.states.CurrentMediaItemState
import com.github.goldy1992.mp3player.client.viewmodels.states.IsPlaying
import com.github.goldy1992.mp3player.client.viewmodels.states.Metadata
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.collect.ImmutableMap
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


    private val _rootItemMap = MutableStateFlow<HashMap<String, List<MediaItem>>>(HashMap())
    val rootItemMap : StateFlow<HashMap<String, List<MediaItem>>> = _rootItemMap

//    private val _rootItemMap : MutableStateFlow<HashMap<String, MutableStateFlow<List<MediaItem>>>> = MutableStateFlow(HashMap())
//    val rootItemMap : StateFlow<HashMap<String, StateFlow<List<MediaItem>>>> = _rootItemMap as StateFlow<HashMap<String, StateFlow<List<MediaItem>>>>

    var rootItem : MediaItem? = null
    private var rootItemId : String? = null
    init {
        viewModelScope.launch {
            val collectedRootItem = mediaBrowserAdapter.getLibraryRoot()
            rootItem = collectedRootItem
            rootItemId = collectedRootItem.mediaId
            mediaBrowserAdapter.subscribe(collectedRootItem.mediaId)
        }

        viewModelScope.launch {
            onChildrenChangedFlow.flow
                .filter {
                    Log.i(logTag(), "filtering: id: ${it.parentId}")
                    val isChildOfARootItem = rootItems.value.map { m -> m.mediaId }.toList().contains(it.parentId)
                    val isRootItem = it.parentId == rootItemId
                    isRootItem || isChildOfARootItem
                }
                .collect {

                if (it.parentId == rootItemId) {
                    val rootChildren = mediaBrowserAdapter.getChildren(it.parentId, 0, it.itemCount)
                    if (rootChildren.isEmpty()) {
                        Log.w(logTag(), "No root children found")
                    } else {
                        _rootItems.value = rootChildren
                        for (mediaItem: MediaItem in rootChildren) {
                            val mediaItemId = mediaItem.mediaId
                            mediaBrowserAdapter.subscribe(mediaItemId)
//                            _rootItemMap.value[mediaItemId] = MutableStateFlow(emptyList())
//                            rootItemMap.value[mediaItemId] = _rootItemMap.value[mediaItemId]!!
                        }
                    }
                } else {//if (rootItemMap.value.containsKey(it.parentId)) {
                    val children = mediaBrowserAdapter.getChildren(it.parentId, 0, it.itemCount)
                    val newMap = HashMap(_rootItemMap.value)
                    newMap[it.parentId] = children
                    _rootItemMap.value = newMap
                }
            }
        }
    }


    private val mediaControllerAsync : ListenableFuture<MediaController> = mediaControllerAdapter.mediaControllerFuture

    val isPlaying = IsPlaying.initialise(this, isPlayingFlow, mainDispatcher, mediaControllerAsync)
    val metadata = Metadata.initialise(this, metadataFlow, mainDispatcher, mediaControllerAsync)
    val currentMediaItem = CurrentMediaItemState.initialise(this, metadataFlow, mainDispatcher, mediaControllerAsync)

    override fun logTag(): String {
        return "LibScrnViewModel"
    }
}