package com.github.goldy1992.mp3player.client.ui.screens.nowplaying

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.RepeatMode
import com.github.goldy1992.mp3player.client.data.audiobands.media.controller.PlaybackStateRepository
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingScreenViewModel
    @Inject
constructor(
    private val playbackStateRepository: PlaybackStateRepository,
) : ViewModel() {

    // playbackPosition
    private val _playbackPositionState = MutableStateFlow(PlaybackPositionEvent.DEFAULT)
    val playbackPosition : StateFlow<PlaybackPositionEvent> = _playbackPositionState

    init {
        viewModelScope.launch {
            playbackStateRepository.playbackPosition().
            shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            ).collect {
                _playbackPositionState.value = it
            }
        }
    }

    // isPlaying
    private val _isPlayingState = MutableStateFlow(false)
    val isPlaying : StateFlow<Boolean> = _isPlayingState

    init {
        viewModelScope.launch {
            playbackStateRepository.isPlaying().
                shareIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(),
                    replay = 1
                ).collect {
                _isPlayingState.value = it
            }
        }
    }


    // metadata
    private val _metadataState = MutableStateFlow(MediaMetadata.EMPTY)
    val metadata : StateFlow<MediaMetadata> = _metadataState

    init {

        viewModelScope.launch {
            playbackStateRepository.metadata().
            shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            ).collect {
                _metadataState.value = it
            }
        }
    }


    // playback speed
    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed : StateFlow<Float> = _playbackSpeed

    init {
        viewModelScope.launch {
            playbackStateRepository.playbackSpeed().
            shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            ).collect {
                _playbackSpeed.value = it
            }
        }
    }


    // queue
    private val _queue = MutableStateFlow(QueueState.EMPTY)
    val queue : StateFlow<QueueState> = _queue

    init {
        viewModelScope.launch {
            playbackStateRepository.queue().            shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            ).collect {
                _queue.value = it
            }
        }
    }


    // repeat mode
    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
    val repeatMode : StateFlow<@RepeatMode Int> = _repeatMode

    init {
        viewModelScope.launch {
            playbackStateRepository.repeatMode().            shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            ).collect {
                _repeatMode.value = it
            }
        }
    }


    // shuffle mode
    private val _shuffleMode = MutableStateFlow(false)
    val shuffleMode : StateFlow<Boolean> = _shuffleMode

    init {
        viewModelScope.launch {
            playbackStateRepository.isShuffleModeEnabled().            shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            ).collect {
                _shuffleMode.value = it
            }
        }
    }

    fun changePlaybackSpeed(speed : Float) {
        viewModelScope.launch { playbackStateRepository.changePlaybackSpeed(speed) }
    }

    fun play() {
        viewModelScope.launch { playbackStateRepository.play() }
    }

    fun pause() {
        viewModelScope.launch { playbackStateRepository.play() }
    }

    fun skipToNext() {
        viewModelScope.launch { playbackStateRepository.skipToNext() }
    }

    fun skipToPrevious() {
        viewModelScope.launch { playbackStateRepository.skipToPrevious() }
    }

    fun seekTo(value : Long) {
        viewModelScope.launch { playbackStateRepository.seekTo(value) }
    }

    fun setShuffleMode(isEnabled: Boolean) {
        viewModelScope.launch { playbackStateRepository.setShuffleMode(isEnabled) }
    }

    fun setRepeatMode(repeatMode: @RepeatMode Int) {
        viewModelScope.launch { playbackStateRepository.setRepeatMode(repeatMode) }
    }
}