package com.github.goldy1992.mp3player.client.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.github.goldy1992.mp3player.client.data.repositories.media.browser.MediaBrowserRepository
import com.github.goldy1992.mp3player.client.data.repositories.media.controller.PlaybackStateRepository
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FolderScreenViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val browserRepository: MediaBrowserRepository,
        private val playbackStateRepository: PlaybackStateRepository
    ) : ViewModel(), LogTagger {

    val folderId : String = checkNotNull(savedStateHandle["folderId"])
    val folderName : String = checkNotNull(savedStateHandle["folderName"])
    val folderPath : String = checkNotNull(savedStateHandle["folderPath"])

    private val _folderChildren : MutableStateFlow<List<MediaItem>> = MutableStateFlow(emptyList())
    // The UI collects from this StateFlow to get its state updates
    val folderChildren : StateFlow<List<MediaItem>> = _folderChildren

    init {
        viewModelScope.launch {
            browserRepository.subscribe(folderId)
            _folderChildren.value = browserRepository.getChildren(folderId).toList()
        }

        viewModelScope.launch {
            browserRepository.onChildrenChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            )
            .filter { it.parentId == folderId }
            .collect {
                browserRepository.getChildren(parentId = folderId)
            }
        }
    }


    // isPlaying
    private val _isPlayingState = MutableStateFlow(false)
    val isPlaying : StateFlow<Boolean> = _isPlayingState

    init {
        viewModelScope.launch {
            playbackStateRepository.isPlaying().
            shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            ).collect {
                _isPlayingState.value = it
            }
        }
    }

    // metadata
    private val _metadataState = MutableStateFlow(MediaMetadata.EMPTY)
    val metadata : StateFlow<MediaMetadata> = _metadataState

    init {
        viewModelScope.launch {
            playbackStateRepository.metadata().
            shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            ).collect {
                _metadataState.value = it
            }
        }
    }

    // currentMediaItem
    private val _currentMediaItemState = MutableStateFlow(MediaItem.EMPTY)
    val currentMediaItem : StateFlow<MediaItem> = _currentMediaItemState

    init {
        viewModelScope.launch {
            playbackStateRepository.currentMediaItem().
            shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            ).collect {
                _currentMediaItemState.value = it
            }
        }
    }

    fun play() {
        viewModelScope.launch { playbackStateRepository.play() }
    }

    fun pause() {
        viewModelScope.launch { playbackStateRepository.play() }
    }

    fun skipToNext() {
        viewModelScope.launch { playbackStateRepository.skipToNext() }
    }

    fun skipToPrevious() {
        viewModelScope.launch { playbackStateRepository.skipToPrevious() }
    }

    fun playFromSongList(index : Int, songs : List<MediaItem>) {
        viewModelScope.launch { playbackStateRepository.playFromSongList(index, songs) }
    }

    override fun logTag(): String {
        return "FolderScreenViewModel"
    }
}