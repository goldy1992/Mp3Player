package com.github.goldy1992.mp3player.client.ui.screens.library

import android.util.Log
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

/**
 * The [ViewModel] implementation for the [LibraryScreen].
 */
@HiltViewModel
class LibraryScreenViewModel
    @Inject
    constructor(
        private val mediaRepository: MediaRepository,
    ) : LogTagger, ViewModel() {

    private val _rootItems : MutableStateFlow<List<MediaItem>> = MutableStateFlow(emptyList())
    val rootItems : StateFlow<List<MediaItem>> = _rootItems


    private val _rootItemMap = MutableStateFlow<HashMap<String, List<MediaItem>>>(HashMap())
    val rootItemMap : StateFlow<HashMap<String, List<MediaItem>>> = _rootItemMap

    private var rootItem : MediaItem? = null
    private var rootItemId : String? = null
    init {
        viewModelScope.launch {
            val collectedRootItem = mediaRepository.getLibraryRoot()
            rootItem = collectedRootItem
            rootItemId = collectedRootItem.mediaId
            mediaRepository.subscribe(collectedRootItem.mediaId)
        }

        viewModelScope.launch {
            mediaRepository.onChildrenChanged()
                .filter {
                    Log.i(logTag(), "filtering: id: ${it.parentId}")
                    val isChildOfARootItem = rootItems.value.map { m -> m.mediaId }.toList().contains(it.parentId)
                    val isRootItem = it.parentId == rootItemId
                    isRootItem || isChildOfARootItem
                }
                .collect {

                if (it.parentId == rootItemId) {
                    val rootChildren = mediaRepository.getChildren(it.parentId, 0, it.itemCount)
                    if (rootChildren.isEmpty()) {
                        Log.w(logTag(), "No root children found")
                    } else {
                        _rootItems.value = rootChildren
                        for (mediaItem: MediaItem in rootChildren) {
                            val mediaItemId = mediaItem.mediaId
                            mediaRepository.subscribe(mediaItemId)
                        }
                    }
                } else {
                    val children = mediaRepository.getChildren(it.parentId, 0, it.itemCount)
                    val newMap = HashMap(_rootItemMap.value)
                    newMap[it.parentId] = children
                    _rootItemMap.value = newMap
                }
            }
        }
    }


    // isPlaying
    private val _isPlayingState = MutableStateFlow(false)
    val isPlaying : StateFlow<Boolean> = _isPlayingState

    init {
        Log.i(logTag(), "init isPlaying")
        viewModelScope.launch {
            mediaRepository.isPlaying()
            .collect {
                Log.i(logTag(), "Current isPlaying: $it")
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

    fun playFromSongList(index : Int, songs : List<MediaItem>) {
        viewModelScope.launch { mediaRepository.playFromSongList(index, songs) }
    }

    fun play() {
        viewModelScope.launch { mediaRepository.play() }
    }

    fun pause() {
        viewModelScope.launch { mediaRepository.pause() }
    }

    fun skipToNext() {
        viewModelScope.launch { mediaRepository.skipToNext() }
    }

    fun skipToPrevious() {
        viewModelScope.launch { mediaRepository.skipToPrevious() }
    }

    override fun logTag(): String {
        return "LibScrnViewModel"
    }
}