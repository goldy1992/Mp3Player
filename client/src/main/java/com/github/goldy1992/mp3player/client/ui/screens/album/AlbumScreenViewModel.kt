package com.github.goldy1992.mp3player.client.ui.screens.album

import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.github.goldy1992.mp3player.client.data.Album
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils
import com.github.goldy1992.mp3player.client.data.MediaEntityUtils.createSongs
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.client.data.Songs
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.Constants.PLAYLIST_ID
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumScreenViewModel
@Inject
constructor(savedStateHandle: SavedStateHandle,
            private val mediaRepository: MediaRepository,
) : ViewModel(), LogTagger  {

    private val albumId : String = checkNotNull(savedStateHandle["albumId"])
    private val albumTitle : String = checkNotNull(savedStateHandle["albumTitle"])
    private val albumArtist : String = checkNotNull(savedStateHandle["albumArtist"])
    private val albumArtUriBase64Encoded : String = checkNotNull(savedStateHandle["albumArtUri"])

    private val albumArtUri : Uri = Uri.parse(String(Base64.decode(albumArtUriBase64Encoded, Base64.DEFAULT)))
    init {
        Log.i(logTag(), "decoded album uri: ${albumArtUri}")
    }
    private val album : Album = Album(
        id = albumId,
        albumTitle = albumTitle,
        albumArtist = albumArtist,
        albumArt = albumArtUri,
    )

    private val _album : MutableStateFlow<Album> = MutableStateFlow(album)
    // The UI collects from this StateFlow to get its state updates
    val albumState : StateFlow<Album> = _album

    init {
        viewModelScope.launch {
            mediaRepository.subscribe(albumId)
            val _albumsChildren = mediaRepository.getChildren(albumId)
            _album.value = mapSongsToAlbum(_albumsChildren)
        }

        viewModelScope.launch {
            mediaRepository.onChildrenChanged()
                .shareIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    replay = 1
                )
                .filter {
                    it.parentId == albumId
                }
                .collect {
                    mediaRepository.getChildren(parentId = albumId)
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

    // currentMediaItem
    private val _currentMediaItemState = MutableStateFlow(Song())
    val currentMediaItem : StateFlow<Song> = _currentMediaItemState

    init {
        viewModelScope.launch {
            mediaRepository.currentMediaItem()
                .collect {
                    _currentMediaItemState.value = MediaEntityUtils.createSong(it)
                }
        }
    }

    // currentMediaItem
    private val _currentPlaylistIdState = MutableStateFlow("")
    val currentPlaylistIdState : StateFlow<String> = _currentPlaylistIdState

    init {
        viewModelScope.launch {
            mediaRepository.currentPlaylistMetadata()
                .collect {
                    val extras = it.extras
                    val playlistId = extras?.getString(PLAYLIST_ID) ?: Constants.UNKNOWN
                    Log.i(logTag(), "new playlist metadata retrieved: $playlistId")
                    _currentPlaylistIdState.value = playlistId
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

    fun playAlbum(index : Int, album: Album) {
        val mediaItems = album.songs.songs.map { MediaItemBuilder(it.id).build() }
        val mediaMetadata = createAlbumPlaylistMetadata(album)
        viewModelScope.launch { mediaRepository.playFromPlaylist(mediaItems, index, mediaMetadata) }
    }



    fun shuffleAlbum(album: Album) {
        val mediaItems = album.songs.songs.map { MediaItemBuilder(it.id).build() }
        viewModelScope.launch {
            mediaRepository.playFromPlaylist( mediaItems, 0, createAlbumPlaylistMetadata(album))
        }
    }


    private fun mapSongsToAlbum(mediaItems : List<MediaItem>) : Album {
        val songs = createSongs(state = State.LOADED, mediaItems)
        val currentAlbum = album
        return Album(
            id = currentAlbum.id,
            albumTitle = currentAlbum.albumTitle,
            albumArtist = currentAlbum.albumArtist,
            albumArt = currentAlbum.albumArt,
            songs = songs,
            totalDuration = songs.totalDuration,
            state = State.LOADED
        )
    }

    private fun createAlbumPlaylistMetadata(album: Album): MediaMetadata {
        val extras = Bundle()
        extras.putString(PLAYLIST_ID, albumId)

        return MediaMetadata.Builder()
            .setMediaType(MediaMetadata.MEDIA_TYPE_FOLDER_MIXED)
            .setAlbumTitle(album.albumTitle)
            .setAlbumArtist(album.albumArtist)
            .setExtras(extras)
            .build()
    }


    override fun logTag(): String {
        return "AlbumScreenViewModel"
    }
}