package com.github.goldy1992.mp3player.client.ui.screens.library

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.data.Albums
import com.github.goldy1992.mp3player.client.data.Folders
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaEntityUtils.createAlbums
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaEntityUtils.createFolders
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaEntityUtils.createPlaylist
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaEntityUtils.createRootItems
import com.github.goldy1992.mp3player.client.data.Playlist
import com.github.goldy1992.mp3player.client.data.RootChild
import com.github.goldy1992.mp3player.client.data.RootChildren
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Pause
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Play
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.PlayPlaylist
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToNext
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToPrevious
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.CurrentSongViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.IsPlayingViewModelState
import com.github.goldy1992.mp3player.commons.Constants.HAS_PERMISSIONS
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.apache.commons.collections4.CollectionUtils.isEmpty
import javax.inject.Inject
import androidx.annotation.OptIn as AndroidXOptIn

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

    private val _rootChildren : MutableStateFlow<RootChildren> = MutableStateFlow(RootChildren())
    val rootChildren : StateFlow<RootChildren> = _rootChildren

    private val idToMediaItemTypeMap = HashMap<String, MediaItemType>()

    private var rootItem : Root? = null
    private var rootItemId : String? = null

    private val _playlist : MutableStateFlow<Playlist> = MutableStateFlow(Playlist.NOT_LOADED)
    val playlist : StateFlow<Playlist> = _playlist

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
            _rootChildren.value = RootChildren.LOADING
        }

        viewModelScope.launch {
            mediaRepository.onChildrenChanged()
                .filter {
                    Log.d(logTag(), "mediaRepository.onChildrenChanged() filter: id: ${it.parentId}")
                    val isChildOfARootItem = isChildOfRootItem(it.parentId)
                    val isRootItem = it.parentId == rootItemId
                    isRootItem || isChildOfARootItem
                }
                .collect {
                    if (it.parentId == rootItemId) {
                        val rootChildren = mediaRepository.getChildren(it.parentId, 0, it.itemCount)
                        if (rootChildren.isEmpty()) {
                            _rootChildren.value = RootChildren.NO_RESULTS
                            Log.w(logTag(), "mediaRepository.onChildrenChanged() collect: No root children found")
                        } else {
                            _rootChildren.value = createRootItems(State.LOADED, rootChildren)

                            for (mediaItem: MediaItem in rootChildren) {
                                val mediaItemId = mediaItem.mediaId
                                mediaRepository.subscribe(mediaItemId)
                                val mediaItemType = MediaItemUtils.getMediaItemType(mediaItem)
                                idToMediaItemTypeMap[mediaItemId] = mediaItemType
                                when (mediaItemType) {
                                    MediaItemType.ALBUMS -> _albums.value = Albums(State.LOADING)
                                    MediaItemType.SONGS -> _playlist.value = Playlist(State.LOADING)
                                    MediaItemType.FOLDERS -> _folders.value = Folders(State.LOADING)
                                    MediaItemType.ROOT -> _rootChildren.value = RootChildren.LOADING
                                    else -> Log.w(logTag(), "mediaRepository.onChildrenChanged() collect: Unsupported MediaItemType: $mediaItemType loaded.")
                                }
                            }
                        }
                    } else {
                        val children = mediaRepository.getChildren(it.parentId, 0, it.itemCount)
                        val mediaItemType = idToMediaItemTypeMap[it.parentId] ?: MediaItemType.NONE

                        if (isEmpty(children)) {
                            if (!hasPermissions(params = it.params!!)) {
                                when (mediaItemType) {
                                    MediaItemType.ALBUMS -> _albums.value = Albums(State.NO_PERMISSIONS)
                                    MediaItemType.SONGS -> _playlist.value = Playlist(State.NO_PERMISSIONS)
                                    MediaItemType.FOLDERS -> _folders.value = Folders(State.NO_PERMISSIONS)
                                    MediaItemType.ROOT -> _rootChildren.value = RootChildren.NO_PERMISSIONS
                                    else -> Log.w(logTag(), "mediaRepository.onChildrenChanged() collect: Unsupported MediaItemType: $mediaItemType loaded.")

                                }
                            } else {
                                when (mediaItemType) {
                                    MediaItemType.ALBUMS -> _albums.value = Albums(State.NO_RESULTS)
                                    MediaItemType.SONGS -> _playlist.value = Playlist(State.NO_RESULTS)
                                    MediaItemType.FOLDERS -> _folders.value = Folders(State.NO_RESULTS)
                                    MediaItemType.ROOT -> _rootChildren.value = RootChildren.NO_RESULTS
                                    else -> Log.w(logTag(), "mediaRepository.onChildrenChanged() collect: Unsupported MediaItemType: $mediaItemType loaded.")
                                }
                            }
                        } else {
                            when (mediaItemType) {
                                MediaItemType.ALBUMS -> _albums.value = createAlbums(State.LOADED, children)
                                MediaItemType.SONGS -> _playlist.value = createPlaylist(State.LOADED, children, id = MediaItemType.SONGS.name)
                                MediaItemType.FOLDERS -> _folders.value = createFolders(State.LOADED, children)
                                MediaItemType.ROOT -> _rootChildren.value = createRootItems(State.LOADED, children)
                                else -> Log.w(logTag(), "mediaRepository.onChildrenChanged() collect: Unsupported MediaItemType: $mediaItemType loaded.")
                            }
                        }
                     }
                }
        }
    }

    val isPlaying = IsPlayingViewModelState(mediaRepository, viewModelScope)
    val currentSong = CurrentSongViewModelState(mediaRepository, viewModelScope)

    private fun isChildOfRootItem(parentId: String) : Boolean {
        return rootChildren.value.items.map(RootChild::id).toList().contains(parentId)
    }

    override fun logTag(): String {
        return "LibScreenViewModel"
    }


    @AndroidXOptIn(UnstableApi::class)
    private fun hasPermissions(params : MediaLibraryService.LibraryParams) : Boolean {
        return params.extras.getBoolean(HAS_PERMISSIONS,false)
    }
}