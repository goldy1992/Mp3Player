package com.github.goldy1992.mp3player.client.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
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
        private val mediaRepository: MediaRepository,
    ) : ViewModel(), LogTagger {

    val folderId : String = checkNotNull(savedStateHandle["folderId"])
    val folderName : String = checkNotNull(savedStateHandle["folderName"])
    val folderPath : String = checkNotNull(savedStateHandle["folderPath"])

    private val _folderChildren : MutableStateFlow<List<MediaItem>> = MutableStateFlow(emptyList())
    // The UI collects from this StateFlow to get its state updates
    val folderChildren : StateFlow<List<MediaItem>> = _folderChildren

    init {
        viewModelScope.launch {
            mediaRepository.subscribe(folderId)
            _folderChildren.value = mediaRepository.getChildren(folderId).toList()
        }

        viewModelScope.launch {
            mediaRepository.onChildrenChanged()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            )
            .filter { it.parentId == folderId }
            .collect {
                mediaRepository.getChildren(parentId = folderId)
            }
        }
    }


    // isPlaying
    private val _isPlayingState = MutableStateFlow(false)
    val isPlaying : StateFlow<Boolean> = _isPlayingState

    init {
        viewModelScope.launch {
            mediaRepository.isPlaying()
            .collect {
                _isPlayingState.value = it
            }
        }
    }

    // metadata
    private val _metadataState = MutableStateFlow(MediaMetadata.EMPTY)
    val metadata : StateFlow<MediaMetadata> = _metadataState

    init {
        viewModelScope.launch {
            mediaRepository.metadata()
            .collect {
                _metadataState.value = it
            }
        }
    }

    // currentMediaItem
    private val _currentMediaItemState = MutableStateFlow(MediaItem.EMPTY)
    val currentMediaItem : StateFlow<MediaItem> = _currentMediaItemState

    init {
        viewModelScope.launch {
            mediaRepository.currentMediaItem()
            .collect {
                _currentMediaItemState.value = it
            }
        }
    }

    fun play() {
        viewModelScope.launch { mediaRepository.play() }
    }

    fun pause() {
        viewModelScope.launch { mediaRepository.play() }
    }

    fun skipToNext() {
        viewModelScope.launch { mediaRepository.skipToNext() }
    }

    fun skipToPrevious() {
        viewModelScope.launch { mediaRepository.skipToPrevious() }
    }

    fun playFromSongList(index : Int, songs : List<MediaItem>) {
        viewModelScope.launch { mediaRepository.playFromSongList(index, songs) }
    }

    override fun logTag(): String {
        return "FolderScreenViewModel"
    }
}