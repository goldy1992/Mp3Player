package com.github.goldy1992.mp3player.client.viewmodels

import androidx.concurrent.futures.await
import androidx.lifecycle.SavedStateHandle
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
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderScreenViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        val mediaBrowser: MediaBrowserAdapter,
        val mediaController: MediaControllerAdapter,
        private val isPlayingFlow: IsPlayingFlow,
        private val metadataFlow: MetadataFlow,
        private val onChildrenChangedFlow: OnChildrenChangedFlow,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher) : ViewModel(), LogTagger {

    val folderId : String = checkNotNull(savedStateHandle["folderId"])
    val folderName : String = checkNotNull(savedStateHandle["folderName"])
    val folderPath : String = checkNotNull(savedStateHandle["folderPath"])
    private val mediaControllerAsync : ListenableFuture<MediaController> = mediaController.mediaControllerFuture

    private val _folderChildren : MutableStateFlow<List<MediaItem>> = MutableStateFlow(emptyList())
    // The UI collects from this StateFlow to get its state updates
    val folderChildren : StateFlow<List<MediaItem>> = _folderChildren

    init {
        viewModelScope.launch {
            mediaBrowser.subscribe(folderId)
            _folderChildren.value = mediaBrowser.getChildren(folderId).toList()
        }

        viewModelScope.launch {
            onChildrenChangedFlow.flow
                .filter { it.parentId == folderId }
                .collect {
                    mediaBrowser.getChildren(parentId = folderId)
                }
        }
      }


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


    // current media item
    private val _currentMediaItemState = MutableStateFlow(MediaItem.EMPTY)
    val currentMediaItem : StateFlow<MediaItem> = _currentMediaItemState

    init {
        viewModelScope.launch(mainDispatcher) {
            _currentMediaItemState.value = mediaControllerAsync.await().currentMediaItem ?: MediaItem.EMPTY
        }
        viewModelScope.launch {
            metadataFlow.flow().collect {
                _currentMediaItemState.value = mediaControllerAsync.await().currentMediaItem ?: MediaItem.EMPTY
            }
        }
    }

    override fun logTag(): String {
        return "FolderScreenViewModel"
    }
}