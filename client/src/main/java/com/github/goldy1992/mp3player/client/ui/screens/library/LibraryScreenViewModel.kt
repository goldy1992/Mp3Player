package com.github.goldy1992.mp3player.client.ui.screens.library

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.states.LibraryResultState
import com.github.goldy1992.mp3player.client.ui.states.LibraryResultState.Companion.loaded
import com.github.goldy1992.mp3player.client.ui.states.LibraryResultState.Companion.loading
import com.github.goldy1992.mp3player.client.ui.states.LibraryResultState.Companion.noResults
import com.github.goldy1992.mp3player.client.ui.states.LibraryResultState.Companion.notLoaded
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils.isEmpty
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

    private val _rootItems : MutableStateFlow<LibraryResultState> = MutableStateFlow(notLoaded(MediaItemType.ROOT))
    val rootItems : StateFlow<LibraryResultState> = _rootItems

    private val idToMediaItemTypeMap = HashMap<String, MediaItemType>()

    private var rootItem : MediaItem? = null
    private var rootItemId : String? = null

    private val _songs : MutableStateFlow<LibraryResultState> = MutableStateFlow(notLoaded(MediaItemType.SONGS))
    val songs : StateFlow<LibraryResultState> = _songs

    private val _folders : MutableStateFlow<LibraryResultState> = MutableStateFlow(notLoaded(MediaItemType.FOLDERS))
    val folders : StateFlow<LibraryResultState> = _folders

    private val _albums : MutableStateFlow<LibraryResultState> = MutableStateFlow(notLoaded(MediaItemType.ALBUMS))
    val albums : StateFlow<LibraryResultState> = _albums
    

    init {
        viewModelScope.launch {
            val collectedRootItem = mediaRepository.getLibraryRoot()
            rootItem = collectedRootItem
            rootItemId = collectedRootItem.mediaId
            mediaRepository.subscribe(collectedRootItem.mediaId)
            _rootItems.value = LibraryResultState(State.LOADING)
        }

        viewModelScope.launch {
            mediaRepository.onChildrenChanged()
                .filter {
                    Log.i(logTag(), "filtering: id: ${it.parentId}")
                    val isChildOfARootItem = isChildOfRootItem(it.parentId)
                    val isRootItem = it.parentId == rootItemId
                    isRootItem || isChildOfARootItem
                }
                .collect {

                if (it.parentId == rootItemId) {
                    val rootChildren = mediaRepository.getChildren(it.parentId, 0, it.itemCount)
                    if (rootChildren.isEmpty()) {
                        _rootItems.value = LibraryResultState(State.NO_RESULTS)
                        Log.w(logTag(), "No root children found")
                    } else {
                        _rootItems.value = LibraryResultState(
                            state = State.LOADED,
                            mediaItemType = MediaItemType.ROOT,
                            results = rootChildren)

                        for (mediaItem: MediaItem in rootChildren) {
                            val mediaItemId = mediaItem.mediaId
                            mediaRepository.subscribe(mediaItemId)
                            val mediaItemType = MediaItemUtils.getMediaItemType(mediaItem)
                            idToMediaItemTypeMap[mediaItemId] = mediaItemType
                            when (mediaItemType) {
                                MediaItemType.ALBUMS -> _albums.value = loading(MediaItemType.ALBUMS)
                                MediaItemType.SONGS -> _songs.value = loading(MediaItemType.SONGS)
                                MediaItemType.FOLDERS -> _folders.value = loading(MediaItemType.FOLDERS)
                                MediaItemType.ROOT -> _rootItems.value = loading(MediaItemType.ROOT)
                                else -> Log.w(logTag(), "Unsupported MediaItemType: $mediaItemType loaded.")
                            }
                        }
                    }
                } else {
                    val children = mediaRepository.getChildren(it.parentId, 0, it.itemCount)
                    val mediaItemType = idToMediaItemTypeMap[it.parentId] ?: MediaItemType.NONE

                    if (isEmpty(children)) {
                        when (mediaItemType) {
                            MediaItemType.ALBUMS -> _albums.value = noResults(MediaItemType.ALBUMS)
                            MediaItemType.SONGS -> _songs.value = noResults(MediaItemType.SONGS)
                            MediaItemType.FOLDERS -> _folders.value = noResults(MediaItemType.FOLDERS)
                            MediaItemType.ROOT -> _rootItems.value = noResults(MediaItemType.ROOT)
                            else -> Log.w(logTag(), "Unsupported MediaItemType: $mediaItemType loaded.")
                        }
                    } else {
                        when (mediaItemType) {
                            MediaItemType.ALBUMS -> _albums.value = loaded(MediaItemType.ALBUMS, children)
                            MediaItemType.SONGS -> _songs.value = loaded(MediaItemType.SONGS, children)
                            MediaItemType.FOLDERS -> _folders.value = loaded(MediaItemType.FOLDERS, children)
                            MediaItemType.ROOT -> _rootItems.value = loaded(MediaItemType.ROOT, children)
                            else -> Log.w(logTag(), "Unsupported MediaItemType: $mediaItemType loaded.")
                        }
                    }
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

    private fun isChildOfRootItem(parentId: String) : Boolean {
        return rootItems.value.results.map { m -> m.mediaId }.toList().contains(parentId)
    }

    override fun logTag(): String {
        return "LibScrnViewModel"
    }

    private fun <K, V> appendToMap(map : Map<K, V>, key : K, value : V) : Map<K, V> {
        val newMap = HashMap(map)
        newMap[key] = value
        return newMap
    }
}