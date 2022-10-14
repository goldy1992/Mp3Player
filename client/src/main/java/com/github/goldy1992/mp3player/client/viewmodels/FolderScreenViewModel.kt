package com.github.goldy1992.mp3player.client.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.client.AsyncPlayerListener
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.flows.mediabrowser.OnChildrenChangedFlow
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.data.flows.player.MetadataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderScreenViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        val mediaBrowser: MediaBrowserAdapter,
        val mediaController: MediaControllerAdapter,
        val metadataFlow: MetadataFlow,
        val isPlayingFlow: IsPlayingFlow,
        val onChildrenChangedFlow: OnChildrenChangedFlow) : ViewModel() {

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
    val folderId : String = checkNotNull(savedStateHandle["folderId"])
    val folderName : String = checkNotNull(savedStateHandle["folderName"])
    val folderPath : String = checkNotNull(savedStateHandle["folderPath"])

    private val _folderChildren : MutableStateFlow<List<MediaItem>> = MutableStateFlow(emptyList())
    // The UI collects from this StateFlow to get its state updates
    val folderChildren : StateFlow<List<MediaItem>> = _folderChildren


}