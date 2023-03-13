package com.github.goldy1992.mp3player.client.ui.screens.nowplaying

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Player.RepeatMode
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingScreenViewModel
    @Inject
constructor(
        private val mediaRepository: MediaRepository,
) : ViewModel(), LogTagger {

    // playbackPosition
    private val _playbackPositionState = MutableStateFlow(PlaybackPositionEvent.DEFAULT)
    val playbackPosition : StateFlow<PlaybackPositionEvent> = _playbackPositionState

    init {
        viewModelScope.launch {
            mediaRepository.playbackPosition()
            .collect {
                _playbackPositionState.value = it
                Log.i(logTag(), "newPlaybackPosition: $it")
            }
        }
    }

    // isPlaying
    private val _isPlayingState = MutableStateFlow(false)
    val isPlaying : StateFlow<Boolean> = _isPlayingState

    init {
        Log.i(logTag(), "init")
        viewModelScope.launch {
            mediaRepository.isPlaying()
            .collect {
                Log.i(logTag(), "isPlaying newState: $it")
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


    // playback speed
    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed : StateFlow<Float> = _playbackSpeed

    init {
        viewModelScope.launch {
            mediaRepository.playbackSpeed()
            .collect {
                _playbackSpeed.value = it
            }
        }
    }


    // queue
    private val _queue = MutableStateFlow(QueueState.EMPTY)
    val queue : StateFlow<QueueState> = _queue

    init {
        viewModelScope.launch {
            mediaRepository.queue()
            .collect {
                Log.i(logTag(), "queue items: ${it.items.size}")
                _queue.value = it
            }
        }
    }


    // repeat mode
    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
    val repeatMode : StateFlow<@RepeatMode Int> = _repeatMode

    init {
        viewModelScope.launch {
            mediaRepository.repeatMode()
            .collect {
                _repeatMode.value = it
            }
        }
    }


    // shuffle mode
    private val _shuffleMode = MutableStateFlow(false)
    val shuffleMode : StateFlow<Boolean> = _shuffleMode

    init {
        viewModelScope.launch {
            mediaRepository.isShuffleModeEnabled()
            .collect {
                _shuffleMode.value = it
            }
        }
    }

    fun changePlaybackSpeed(speed : Float) {
        viewModelScope.launch { mediaRepository.changePlaybackSpeed(speed) }
    }

    fun play() {
        viewModelScope.launch {
            Log.i(logTag(), "calling play()")
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