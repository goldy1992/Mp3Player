package com.github.goldy1992.mp3player.client.ui.screens.library

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.client.data.Albums
import com.github.goldy1992.mp3player.client.data.Folders
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createAlbums
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createFolders
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createRootItems
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createSong
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createSongs
import com.github.goldy1992.mp3player.client.data.RootItem
import com.github.goldy1992.mp3player.client.data.RootItems
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.data.Songs
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.Constants.HAS_PERMISSIONS
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
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
        private val mediaRepository: MediaRepository
    ) : LogTagger, ViewModel() {

    private val _rootItems : MutableStateFlow<RootItems> = MutableStateFlow(RootItems())
    val rootItems : StateFlow<RootItems> = _rootItems

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
            _rootItems.value = RootItems.LOADING
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
                            _rootItems.value = RootItems.NO_RESULTS
                            Log.w(logTag(), "mediaRepository.onChildrenChanged() collect: No root children found")
                        } else {
                            _rootItems.value = createRootItems(State.LOADED, rootChildren)

                            for (mediaItem: MediaItem in rootChildren) {
                                val mediaItemId = mediaItem.mediaId
                                mediaRepository.subscribe(mediaItemId)
                                val mediaItemType = MediaItemUtils.getMediaItemType(mediaItem)
                                idToMediaItemTypeMap[mediaItemId] = mediaItemType
                                when (mediaItemType) {
                                    MediaItemType.ALBUMS -> _albums.value = Albums(State.LOADING)
                                    MediaItemType.SONGS -> _songs.value = Songs(State.LOADING)
                                    MediaItemType.FOLDERS -> _folders.value = Folders(State.LOADING)
                                    MediaItemType.ROOT -> _rootItems.value = RootItems.LOADING
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
                                    MediaItemType.SONGS -> _songs.value = Songs(State.NO_PERMISSIONS)
                                    MediaItemType.FOLDERS -> _folders.value = Folders(State.NO_PERMISSIONS)
                                    MediaItemType.ROOT -> _rootItems.value = RootItems.NO_PERMISSIONS
                                    else -> Log.w(logTag(), "mediaRepository.onChildrenChanged() collect: Unsupported MediaItemType: $mediaItemType loaded.")

                                }
                            } else {
                                when (mediaItemType) {
                                    MediaItemType.ALBUMS -> _albums.value = Albums(State.NO_RESULTS)
                                    MediaItemType.SONGS -> _songs.value = Songs(State.NO_RESULTS)
                                    MediaItemType.FOLDERS -> _folders.value = Folders(State.NO_RESULTS)
                                    MediaItemType.ROOT -> _rootItems.value = RootItems.NO_RESULTS
                                    else -> Log.w(logTag(), "mediaRepository.onChildrenChanged() collect: Unsupported MediaItemType: $mediaItemType loaded.")
                                }
                            }
                        } else {
                            when (mediaItemType) {
                                MediaItemType.ALBUMS -> _albums.value = createAlbums(State.LOADED, children)
                                MediaItemType.SONGS -> _songs.value = createSongs(State.LOADED, children)
                                MediaItemType.FOLDERS -> _folders.value = createFolders(State.LOADED, children)
                                MediaItemType.ROOT -> _rootItems.value = createRootItems(State.LOADED, children)
                                else -> Log.w(logTag(), "mediaRepository.onChildrenChanged() collect: Unsupported MediaItemType: $mediaItemType loaded.")
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
        Log.v(logTag(), "init isPlaying")
        viewModelScope.launch {
            mediaRepository.isPlaying()
            .collect {
                Log.d(logTag(), "mediaRepository.isPlaying() collect: current isPlaying: $it")
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
                Log.v(logTag(), "mediaRepository.currentMediaItem() collect: new current media item - id : ${it.mediaId}, title: ${it.mediaMetadata.title}")
                _currentMediaItemState.value = createSong(it)
            }
        }
    }

    fun playPlaylist(playlistId : String, songs : Songs, index : Int) {
        val mediaItems = songs.songs.map { MediaItemBuilder(it.id).build() }
        val extras = Bundle()
        extras.putString(Constants.PLAYLIST_ID, MediaItemType.SONGS.name)

        val mediaMetadata = MediaMetadata.Builder()
            .setAlbumTitle(MediaItemType.SONGS.name)
            .setExtras(extras)
            .build()
        viewModelScope.launch { mediaRepository.playFromPlaylist(mediaItems, index, mediaMetadata) }
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
        return rootItems.value.items.map(RootItem::id).toList().contains(parentId)
    }

    override fun logTag(): String {
        return "LibScreenViewModel"
    }


    @AndroidXOptIn(UnstableApi::class)
    private fun hasPermissions(params : MediaLibraryService.LibraryParams) : Boolean {
        return params.extras.getBoolean(HAS_PERMISSIONS,false)
    }
}