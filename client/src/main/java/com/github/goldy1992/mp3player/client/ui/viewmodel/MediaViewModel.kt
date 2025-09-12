package com.github.goldy1992.mp3player.client.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.models.media.Album
import com.github.goldy1992.mp3player.client.models.media.MediaActions
import com.github.goldy1992.mp3player.client.models.media.PlaybackState
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Pause
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Play
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToNext
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToPrevious
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Adds all the common States and Actions for the common Play Toolbar.
 */
abstract class MediaViewModel(
    final override val mediaRepository: MediaRepository,
) : Pause, Play, SkipToNext, SkipToPrevious, ViewModel() {

    companion object {
        const val LOG_TAG = "MediaViewModel"
    }
    final override val scope: CoroutineScope = viewModelScope

    private val _playbackState = MutableStateFlow(
        PlaybackState(
        )
    )
    val playbackState : StateFlow<PlaybackState> = _playbackState
    
    val actions : MediaActions = MediaActions(
        play = { play() },
        pause = { pause() },
        skipToNext = { skipToNext() },
        skipToPrevious = { skipToPrevious() },
        onPlayAlbum =  {   currentPlaylistId, album -> if (isCurrentPlaylistTheSelectedAlbum(currentPlaylistId, album)) {
            play()
        } else {
            playAlbum(0, album)
        }},
        isAlbumPlaying = { isPlaying, currentPlaylistId, album -> isAlbumPlaying(isPlaying, currentPlaylistId, album)},
        onAlbumSongSelected = { itemIndex, album ->
            playAlbum(itemIndex, album)
        }
    )
    init {
        scope.launch {
            mediaRepository.isPlaying()
                .collect {
                    Log.i(LOG_TAG, "current playbackState: ${playbackState.value}")
                    _playbackState.value = PlaybackState(
                        isPlaying = it,
                        currentSong = playbackState.value.currentSong,
                        repeatMode = playbackState.value.repeatMode,
                        shuffleEnabled = playbackState.value.shuffleEnabled,
                        playbackSpeed = playbackState.value.playbackSpeed,
                        playbackPosition = playbackState.value.playbackPosition,
                        actions = actions
                    )
                }
        }    
        
        scope.launch {
            mediaRepository.currentSong()
                .collect {
                    Log.v(LOG_TAG, "currentSong collected with id: ${it.title}")
                    _playbackState.value = PlaybackState(
                        isPlaying = playbackState.value.isPlaying,
                        currentSong = it,
                        repeatMode = playbackState.value.repeatMode,
                        shuffleEnabled = playbackState.value.shuffleEnabled,
                        playbackSpeed = playbackState.value.playbackSpeed,
                        playbackPosition = playbackState.value.playbackPosition,
                        actions = actions
                    )
                }
        }
        scope.launch {
            mediaRepository.repeatMode()
                .collect {
                    _playbackState.value = PlaybackState(
                        isPlaying = playbackState.value.isPlaying,
                        currentSong = playbackState.value.currentSong,
                        repeatMode = it,
                        shuffleEnabled = playbackState.value.shuffleEnabled,
                        playbackSpeed = playbackState.value.playbackSpeed,
                        playbackPosition = playbackState.value.playbackPosition,
                        actions = actions
                    )
                }
        }

        scope.launch {
            mediaRepository.isShuffleModeEnabled()
                .collect {
                    _playbackState.value = PlaybackState(
                        isPlaying = playbackState.value.isPlaying,
                        currentSong = playbackState.value.currentSong,
                        repeatMode = playbackState.value.repeatMode,
                        shuffleEnabled = it,
                        playbackSpeed = playbackState.value.playbackSpeed,
                        playbackPosition = playbackState.value.playbackPosition,
                        actions = actions
                    )
                }
        }

        scope.launch {
            mediaRepository.playbackSpeed()
                .collect {
                    _playbackState.value = PlaybackState(
                        isPlaying = playbackState.value.isPlaying,
                        currentSong = playbackState.value.currentSong,
                        repeatMode = playbackState.value.repeatMode,
                        shuffleEnabled = playbackState.value.shuffleEnabled,
                        playbackSpeed = it,
                        playbackPosition = playbackState.value.playbackPosition,
                        actions = actions
                    )
                }
        }

        viewModelScope.launch {
            mediaRepository.playbackPosition()
                .collect {
                    _playbackState.value = PlaybackState(
                        isPlaying = playbackState.value.isPlaying,
                        currentSong = playbackState.value.currentSong,
                        repeatMode = playbackState.value.repeatMode,
                        shuffleEnabled = playbackState.value.shuffleEnabled,
                        playbackSpeed = playbackState.value.playbackSpeed,
                        playbackPosition = it,
                        actions = actions
                    )
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

    private fun isCurrentPlaylistTheSelectedAlbum(currentPlaylistId : String, album: Album) : Boolean {
        return currentPlaylistId == album.id
    }

    private fun isAlbumPlaying(
        isPlaying: Boolean,
        currentPlaylistId: String,
        album: Album
    ) = isPlaying && isCurrentPlaylistTheSelectedAlbum(currentPlaylistId, album)

}