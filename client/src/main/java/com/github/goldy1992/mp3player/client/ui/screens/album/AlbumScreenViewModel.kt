package com.github.goldy1992.mp3player.client.ui.screens.album

import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.models.Album
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.models.State
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Pause
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Play
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SetShuffleEnabled
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.ShufflePlayPlaylist
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToNext
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToPrevious
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.CurrentSongViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.IsPlayingViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.ShuffleModeViewModelState
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumScreenViewModel
@Inject
constructor(savedStateHandle: SavedStateHandle,
            override val mediaRepository: MediaRepository,
) : Play, Pause, SetShuffleEnabled, ShufflePlayPlaylist, SkipToNext, SkipToPrevious, ViewModel(), LogTagger  {

    override val scope = viewModelScope
    private val albumId : String = checkNotNull(savedStateHandle["albumId"])
    private val albumTitle : String = checkNotNull(savedStateHandle["albumTitle"])
    private val albumArtist : String = checkNotNull(savedStateHandle["albumArtist"])
    private val albumArtUriBase64Encoded : String = checkNotNull(savedStateHandle["albumArtUri"])

    private val albumArtUri : Uri = Uri.parse(String(Base64.decode(albumArtUriBase64Encoded, Base64.DEFAULT)))
    init {
        Log.d(logTag(), "AlbumScreenViewModel init, decoded album uri: $albumArtUri")
    }

    private val _album : MutableStateFlow<Album> = MutableStateFlow(Album(
        id = albumId,
        albumTitle = albumTitle,
        albumArtist = albumArtist,
        albumArt = albumArtUri,
    ))
    // The UI collects from this StateFlow to get its state updates
    val albumState : StateFlow<Album> = _album

    init {
        viewModelScope.launch {
            mediaRepository.subscribe(albumId)
            val albumPlaylist = mediaRepository.getPlaylist(albumId)
            val currentAlbum = _album.value
            _album.value = Album(
                id = currentAlbum.id,
                albumTitle = currentAlbum.albumTitle,
                albumArtist = currentAlbum.albumArtist,
                albumArt = currentAlbum.albumArt,
                playlist = albumPlaylist,
                state = State.LOADED
            )
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
                    val currentAlbum = _album.value
                    _album.value = Album(
                        id =currentAlbum.id,
                        type=currentAlbum.type,
                        albumTitle=currentAlbum.albumTitle,
                        albumArtist=currentAlbum.albumArtist,
                        recordingYear=currentAlbum.recordingYear,
                        releaseYear=currentAlbum.releaseYear,
                        playlist = mediaRepository.getPlaylist(currentAlbum.id),
                        totalDuration = currentAlbum.totalDuration,
                        albumArt = currentAlbum.albumArt
                    )
                }
        }
    }

    val isPlaying = IsPlayingViewModelState(mediaRepository, viewModelScope)
    val shuffleEnabled = ShuffleModeViewModelState(mediaRepository, viewModelScope)
    val currentSong = CurrentSongViewModelState(mediaRepository, viewModelScope)


    // currentMediaItem
    private val _currentPlaylistIdState = MutableStateFlow("")
    val currentPlaylistIdState : StateFlow<String> = _currentPlaylistIdState

    init {
        viewModelScope.launch {
            mediaRepository.currentPlaylistId()
                .collect {
                    _currentPlaylistIdState.value = it
                }
        }
    }


    fun playAlbum(index : Int, album: Album) {
        viewModelScope.launch { mediaRepository.playPlaylist(album.playlist, index) }
    }



    fun shuffleAlbum(album: Album) {
        viewModelScope.launch {
            mediaRepository.playPlaylist( album.playlist, 0)
        }
    }


    override fun logTag(): String {
        return "AlbumScreenViewModel"
    }
}