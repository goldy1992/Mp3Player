package com.github.goldy1992.mp3player.client.ui.screens.library

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.models.Albums
import com.github.goldy1992.mp3player.client.models.Folders
import com.github.goldy1992.mp3player.client.models.MediaEntity
import com.github.goldy1992.mp3player.client.models.Playlist
import com.github.goldy1992.mp3player.client.models.Root
import com.github.goldy1992.mp3player.client.models.State
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Pause
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Play
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.PlayPlaylist
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToNext
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToPrevious
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.CurrentSongViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.IsPlayingViewModelState
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
                            val albums = rootItem.childMap[MediaItemType.ALBUMS] as Albums
                            _albums.value = Albums(
                                id = albums.id,
                                state = State.LOADING
                            )
                            mediaRepository.subscribe(albums.id)

                            val folders = rootItem.childMap[MediaItemType.FOLDERS] as Folders
                            _folders.value = Folders(
                                id = folders.id,
                                state = State.LOADING
                            )
                            mediaRepository.subscribe(folders.id)

                            val songs = rootItem.childMap[MediaItemType.SONGS] as Playlist
                            _songs.value = Playlist(
                                id = songs.id,
                                state = State.LOADING
                            )
                            mediaRepository.subscribe(songs.id)

                        }

                        albums.value.id -> {
                            _albums.value =
                                mediaRepository.getChildren(albums.value, 0, it.itemCount)
                        }

                        folders.value.id -> {
                            _folders.value =
                                mediaRepository.getChildren(folders.value, 0, it.itemCount)
                        }

                        songs.value.id -> {
                            _songs.value = mediaRepository.getChildren(songs.value, 0, it.itemCount)
                        }

                        else -> {
                            Log.w(logTag(), "Received unknown id notification: ${it.parentId}")
                        }
                    }
                }
//                    else {
//
//                        val children = mediaRepository.getChildren(it.parentId, 0, it.itemCount)
//                        val mediaItemType = idToMediaItemTypeMap[it.parentId] ?: MediaItemType.NONE
//
//                        if (isEmpty(children)) {
//                            if (!hasPermissions(params = it.params!!)) {
//                                when (mediaItemType) {
//                                    MediaItemType.ALBUMS -> _albums.value = Albums(State.NO_PERMISSIONS)
//                                    MediaItemType.SONGS -> _songs.value = Playlist(State.NO_PERMISSIONS)
//                                    MediaItemType.FOLDERS -> _folders.value = Folders(State.NO_PERMISSIONS)
//                                    MediaItemType.ROOT -> _rootChildren.value = RootChildren.NO_PERMISSIONS
//                                    else -> Log.w(logTag(), "mediaRepository.onChildrenChanged() collect: Unsupported MediaItemType: $mediaItemType loaded.")
//
//                                }
//                            } else {
//                                when (mediaItemType) {
//                                    MediaItemType.ALBUMS -> _albums.value = Albums(State.NO_RESULTS)
//                                    MediaItemType.SONGS -> _songs.value = Playlist(State.NO_RESULTS)
//                                    MediaItemType.FOLDERS -> _folders.value = Folders(State.NO_RESULTS)
//                                    MediaItemType.ROOT -> _rootChildren.value = RootChildren.NO_RESULTS
//                                    else -> Log.w(logTag(), "mediaRepository.onChildrenChanged() collect: Unsupported MediaItemType: $mediaItemType loaded.")
//                                }
//                            }
//                        } else {
//                            when (mediaItemType) {
//                                MediaItemType.ALBUMS -> _albums.value = createAlbums(State.LOADED, children)
//                                MediaItemType.SONGS -> _songs.value = createPlaylist(State.LOADED, children, id = MediaItemType.SONGS.name)
//                                MediaItemType.FOLDERS -> _folders.value = createFolders(State.LOADED, children)
//                                MediaItemType.ROOT -> _rootChildren.value = createRootItems(State.LOADED, children)
//                                else -> Log.w(logTag(), "mediaRepository.onChildrenChanged() collect: Unsupported MediaItemType: $mediaItemType loaded.")
//                            }
//                        }

        }
    }

    val isPlaying = IsPlayingViewModelState(mediaRepository, viewModelScope)
    val currentSong = CurrentSongViewModelState(mediaRepository, viewModelScope)

    private fun isChildOfRootItem(parentId: String) : Boolean {
        return root.value.childMap.values.map(MediaEntity::id).toList().contains(parentId)
    }

    override fun logTag(): String {
        return "LibScreenViewModel"
    }

}