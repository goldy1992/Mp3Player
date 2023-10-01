package com.github.goldy1992.mp3player.client.ui.screens.library

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaEntityUtils.setStateNoPermissions
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.models.media.Albums
import com.github.goldy1992.mp3player.client.models.media.Folders
import com.github.goldy1992.mp3player.client.models.media.MediaEntity
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.client.models.media.State
import com.github.goldy1992.mp3player.client.utils.ExtrasUtils.hasPermissions
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Pause
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Play
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.PlayPlaylist
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToNext
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToPrevious
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.CurrentSongViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.IsPlayingViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.PlaybackPositionViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.PlaybackSpeedViewModelState
import com.github.goldy1992.mp3player.commons.MediaItemType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The [ViewModel] implementation for the [LibraryScreen].
 */
@HiltViewModel
class LibraryScreenViewModel
    @Inject
    constructor(
        override val mediaRepository: MediaRepository
    ) : Pause, Play, PlayPlaylist, SkipToNext, SkipToPrevious, ViewModel() {
    override val scope = viewModelScope


    private val _root = MutableStateFlow(Root.NOT_LOADED)
    val root : StateFlow<Root> = _root

    private val _selectedChip = MutableStateFlow(SelectedLibraryItem.NONE)
    val selectedChip : StateFlow<SelectedLibraryItem> = _selectedChip

    private val _songs : MutableStateFlow<Playlist> = MutableStateFlow(Playlist.NOT_LOADED)
    val songs : StateFlow<Playlist> = _songs

    private val _folders : MutableStateFlow<Folders> = MutableStateFlow(Folders.NOT_LOADED)
    val folders : StateFlow<Folders> = _folders

    private val _albums : MutableStateFlow<Albums> = MutableStateFlow(Albums.NOT_LOADED)
    val albums : StateFlow<Albums> = _albums

    init {
        viewModelScope.launch {
            val collectedRootItem = mediaRepository.getLibraryRoot()
            mediaRepository.subscribe(collectedRootItem.id)
            _root.value = Root(collectedRootItem.id, State.LOADING)
        }

        viewModelScope.launch {
            mediaRepository.onChildrenChanged()
                .filter {
                    Log.d(logTag(), "mediaRepository.onChildrenChanged() filter: id: ${it.parentId}")
                    val isChildOfARootItem = isChildOfRootItem(it.parentId)
                    val isRootItem = it.parentId == _root.value.id
                    isRootItem || isChildOfARootItem
                }
                .collect {
                    when (it.parentId) {
                        root.value.id -> {
                            val rootItem = mediaRepository.getChildren(root.value, 0, it.itemCount)
                            _root.value = rootItem
                            if (rootItem.childMap.containsKey(MediaItemType.ALBUMS)) {
                                val albums = rootItem.childMap[MediaItemType.ALBUMS] as Albums
                                _albums.value = Albums(
                                    id = albums.id,
                                    state = State.LOADING
                                )
                                mediaRepository.subscribe(albums.id)
                            }

                            if (rootItem.childMap.containsKey(MediaItemType.FOLDERS)) {
                                val folders = rootItem.childMap[MediaItemType.FOLDERS] as Folders
                                _folders.value = Folders(
                                    id = folders.id,
                                    state = State.LOADING
                                )
                                mediaRepository.subscribe(folders.id)
                            }

                            if (rootItem.childMap.containsKey(MediaItemType.SONGS)) {
                                val songs = rootItem.childMap[MediaItemType.SONGS] as Playlist
                                _songs.value = Playlist(
                                    id = songs.id,
                                    state = State.LOADING
                                )
                                mediaRepository.subscribe(songs.id)
                            }
                        }

                        albums.value.id -> {
                            _albums.value = if (hasPermissions(it.extras)) {
                                mediaRepository.getChildren(albums.value, 0, it.itemCount)
                            } else {
                                setStateNoPermissions(albums.value)
                            }
                        }

                        folders.value.id -> {
                            _folders.value = if (hasPermissions(it.extras)) {
                                mediaRepository.getChildren(folders.value, 0, it.itemCount)
                            } else {
                                setStateNoPermissions(folders.value)
                            }
                        }

                        songs.value.id -> {
                            _songs.value = if (hasPermissions(it.extras)) {
                                mediaRepository.getChildren(songs.value, 0, it.itemCount)
                            } else {
                                setStateNoPermissions(songs.value)
                            }
                        }


                        else -> {
                            Log.w(logTag(), "Received unknown id notification: ${it.parentId}")
                        }
                    }
                }
        }
    }

    val isPlaying = IsPlayingViewModelState(mediaRepository, viewModelScope)
    val currentSong = CurrentSongViewModelState(mediaRepository, viewModelScope)
    val playbackSpeed = PlaybackSpeedViewModelState(mediaRepository, viewModelScope)
    val playbackPosition = PlaybackPositionViewModelState(mediaRepository, viewModelScope)
    private fun isChildOfRootItem(parentId: String) : Boolean {
        return root.value.childMap.values.map(MediaEntity::id).toList().contains(parentId)
    }

    fun setSelectedChip(chip: SelectedLibraryItem) {
        _selectedChip.value = chip
    }

    override fun logTag(): String {
        return "LibScreenViewModel"
    }

}