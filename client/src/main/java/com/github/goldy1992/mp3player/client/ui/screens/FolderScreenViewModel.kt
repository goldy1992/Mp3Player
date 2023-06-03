package com.github.goldy1992.mp3player.client.ui.screens

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.github.goldy1992.mp3player.client.data.Folder
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createSong
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createSongs
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.data.Songs
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.Constants.PLAYLIST_ID
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils.isEmpty
import javax.inject.Inject

@HiltViewModel
class FolderScreenViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val mediaRepository: MediaRepository,
    ) : ViewModel(), LogTagger {

    private val folderId : String = checkNotNull(savedStateHandle["folderId"])
    private val folderName : String = checkNotNull(savedStateHandle["folderName"])
    private val folderPath : String = checkNotNull(savedStateHandle["folderPath"])

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
    private val _currentMediaItemState = MutableStateFlow(Song())
    val currentMediaItem : StateFlow<Song> = _currentMediaItemState

    init {
        viewModelScope.launch {
            mediaRepository.currentMediaItem()
            .collect {
                _currentMediaItemState.value = createSong(it)
            }
        }
    }

    private val _folder = MutableStateFlow(
        Folder(
            id = folderId,
            name = folderName,
            path = folderPath
        )
    )
    val folder : StateFlow<Folder> = _folder

    init {
        viewModelScope.launch {
            mediaRepository.onChildrenChanged()
                .shareIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    replay = 1
                )
                .filter { it.parentId == folderId }
                .collect {
                    val mediaItems = mediaRepository.getChildren(parentId = folderId)
                    val currentFolderValue = folder.value
                    if (isEmpty(mediaItems)) {
                        _folder.value = Folder(
                            name = currentFolderValue.name,
                            path = currentFolderValue.path,
                            uri = currentFolderValue.uri,
                            songs = Songs(State.NO_RESULTS),
                            state = State.NO_RESULTS
                        )
                    } else {
                        val songs = createSongs(State.LOADED, mediaItems)
                        _folder.value = Folder(
                            name = currentFolderValue.name,
                            path = currentFolderValue.path,
                            uri = currentFolderValue.uri,
                            songs = songs,
                            totalDuration = songs.totalDuration,
                            state = State.LOADED
                        )
                    }
                }
        }
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

    fun playPlaylist(songs: Songs, index : Int) {
        val mediaItems = songs.songs.map { MediaItemBuilder(it.id).build() }
        val extras = Bundle()
        extras.putString(PLAYLIST_ID, folderId)

        val mediaMetadata = MediaMetadata.Builder()
            .setAlbumTitle(folder.value.name)
            .setExtras(extras)
            .build()
        viewModelScope.launch { mediaRepository.playFromPlaylist(mediaItems, index, mediaMetadata) }
    }

    override fun logTag(): String {
        return "FolderScreenViewModel"
    }
}