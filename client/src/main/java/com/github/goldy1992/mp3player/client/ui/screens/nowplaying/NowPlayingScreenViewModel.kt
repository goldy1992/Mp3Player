package com.github.goldy1992.mp3player.client.ui.screens.nowplaying

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import androidx.media3.common.Player.RepeatMode
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.viewmodel.CurrentSongViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.IsPlayingViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.PlaybackPositionViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.PlaybackSpeedViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.QueueViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.RepeatModeViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.ShuffleModeViewModelState
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] for the [NowPlayingScreen]
 */
@HiltViewModel
class NowPlayingScreenViewModel
    @Inject
constructor(
        private val mediaRepository: MediaRepository,
) : ViewModel(), LogTagger {

    val playbackPosition = PlaybackPositionViewModelState(mediaRepository, viewModelScope)
    val isPlaying = IsPlayingViewModelState(mediaRepository, viewModelScope)
    val playbackSpeed = PlaybackSpeedViewModelState(mediaRepository, viewModelScope)
    val currentSong = CurrentSongViewModelState(mediaRepository, viewModelScope)
    val queue = QueueViewModelState(mediaRepository, viewModelScope)
    val repeatMode = RepeatModeViewModelState(mediaRepository, viewModelScope)
    val shuffleMode = ShuffleModeViewModelState(mediaRepository, viewModelScope)

    fun changePlaybackSpeed(speed : Float) {
        viewModelScope.launch { mediaRepository.changePlaybackSpeed(speed) }
    }

    fun play() {
        viewModelScope.launch {
            Log.v(logTag(), "play() invoked")
            mediaRepository.play() }
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

    fun seekTo(value : Long) {
        viewModelScope.launch { mediaRepository.seekTo(value) }
    }

    fun setShuffleMode(isEnabled: Boolean) {
        viewModelScope.launch { mediaRepository.setShuffleMode(isEnabled) }
    }

    fun setRepeatMode(repeatMode: @RepeatMode Int) {
        viewModelScope.launch { mediaRepository.setRepeatMode(repeatMode) }
    }

    override fun logTag(): String {
        return "NowPlayingViewModel"
    }
}