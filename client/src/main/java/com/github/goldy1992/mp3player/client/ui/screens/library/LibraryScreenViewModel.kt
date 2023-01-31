package com.github.goldy1992.mp3player.client.ui.screens.library

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.github.goldy1992.mp3player.client.data.Albums
import com.github.goldy1992.mp3player.client.data.Folders
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createAlbums
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createFolders
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createSong
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createSongs
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.data.Songs
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.states.LibraryResultState
import com.github.goldy1992.mp3player.client.ui.states.LibraryResultState.Companion.loaded
import com.github.goldy1992.mp3player.client.ui.states.LibraryResultState.Companion.loading
import com.github.goldy1992.mp3player.client.ui.states.LibraryResultState.Companion.noResults
import com.github.goldy1992.mp3player.client.ui.states.LibraryResultState.Companion.notLoaded
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
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

    private val _songs : MutableStateFlow<Songs> = MutableStateFlow(Songs.NOT_LOADED)
    val songs : StateFlow<Songs> = _songs

    private val _folders : MutableStateFlow<Folders> = MutableStateFlow(Folders(State.NOT_LOADED))
    val folders : StateFlow<Folders> = _folders

    private val _albums : MutableStateFlow<Albums> = MutableStateFlow(Albums(State.NOT_LOADED))
    val albums : StateFlow<Albums> = _albums

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
                                MediaItemType.ALBUMS -> _albums.value = Albums(State.LOADING)
                                MediaItemType.SONGS -> _songs.value = Songs(State.LOADING)
                                MediaItemType.FOLDERS -> _folders.value = Folders(State.LOADING)
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
                            MediaItemType.ALBUMS -> _albums.value = Albums(State.NO_RESULTS)
                            MediaItemType.SONGS -> _songs.value = Songs(State.NO_RESULTS)
                            MediaItemType.FOLDERS -> _folders.value = Folders(State.NO_RESULTS)
                            MediaItemType.ROOT -> _rootItems.value = noResults(MediaItemType.ROOT)
                            else -> Log.w(logTag(), "Unsupported MediaItemType: $mediaItemType loaded.")
                        }
                    } else {
                        when (mediaItemType) {
                            MediaItemType.ALBUMS -> _albums.value = createAlbums(State.LOADED, children)
                            MediaItemType.SONGS -> _songs.value = createSongs(State.LOADED, children)
                            MediaItemType.FOLDERS -> _folders.value = createFolders(State.LOADED, children)
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
    private val _currentMediaItemState = MutableStateFlow(Song())
    val currentMediaItem : StateFlow<Song> = _currentMediaItemState

    init {
        viewModelScope.launch {
            mediaRepository.currentMediaItem()
            .collect {
                Log.i(logTag(), "new current media item - id : ${it.mediaId}, title: ${it.mediaMetadata.title}")
                _currentMediaItemState.value = createSong(it)
            }
        }
    }

    fun playFromSongList(index : Int, songs : Songs) {
        val mediaItems = songs.songs.map { MediaItemBuilder(it.id).build() }
        viewModelScope.launch { mediaRepository.playFromSongList(index, mediaItems) }
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

}