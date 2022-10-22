package com.github.goldy1992.mp3player.client.viewmodels

import androidx.concurrent.futures.await
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.Player.RepeatMode
import androidx.media3.session.MediaController
import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.flows.player.*
import com.github.goldy1992.mp3player.commons.MainDispatcher
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NowPlayingScreenViewModel
    @Inject
constructor(
    val mediaBrowserAdapter: MediaBrowserAdapter,
    val mediaControllerAdapter: MediaControllerAdapter,
    private val isPlayingFlow: IsPlayingFlow,
    private val metadataFlow: MetadataFlow,
    private val playbackSpeedFlow: PlaybackSpeedFlow,
    private val queueFlow: QueueFlow,
    private val repeatModeFlow: RepeatModeFlow,
    private val shuffleModeFlow: ShuffleModeFlow,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val mediaControllerAsync : ListenableFuture<MediaController> = mediaControllerAdapter.mediaControllerFuture

    // isPlaying
    private val _isPlayingState = MutableStateFlow(false)
    val isPlaying : StateFlow<Boolean> = _isPlayingState

    init {
        viewModelScope.launch(mainDispatcher) {
            _isPlayingState.value = mediaControllerAsync.await().isPlaying
        }
        viewModelScope.launch {
            isPlayingFlow.flow().collect {
                _isPlayingState.value = it
            }
        }
    }


    // metadata
    private val _metadataState = MutableStateFlow(MediaMetadata.EMPTY)
    val metadata : StateFlow<MediaMetadata> = _metadataState

    init {
        viewModelScope.launch(mainDispatcher) {
            _metadataState.value = mediaControllerAsync.await().mediaMetadata
        }
        viewModelScope.launch {
            metadataFlow.flow().collect {
                _metadataState.value = it
            }
        }
    }


    // playback speed
    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed : StateFlow<Float> = _playbackSpeed

    init {
        viewModelScope.launch(mainDispatcher) {
            _playbackSpeed.value = mediaControllerAsync.await().playbackParameters.speed
        }
        viewModelScope.launch {
            playbackSpeedFlow.flow().collect {
                _playbackSpeed.value = it
            }
        }
    }


    // queue
    private val _queue = MutableStateFlow(emptyList<MediaItem>())
    val queue : StateFlow<List<MediaItem>> = _queue

    init {
        viewModelScope.launch(mainDispatcher) {
           _queue.value = queueFlow.getQueue(mediaControllerAsync.await())
        }
        viewModelScope.launch {
            queueFlow.flow().collect {
                _queue.value = it
            }
        }
    }


    // repeat mode
    private val _repeatMode = MutableStateFlow(Player.REPEAT_MODE_OFF)
    val repeatMode : StateFlow<@RepeatMode Int> = _repeatMode

    init {
        viewModelScope.launch(mainDispatcher) {
            _repeatMode.value = mediaControllerAsync.await().repeatMode
        }
        viewModelScope.launch {
            repeatModeFlow.flow().collect {
                _repeatMode.value = it
            }
        }
    }


    // shuffle mode
    private val _shuffleMode = MutableStateFlow(false)
    val shuffleMode : StateFlow<Boolean> = _shuffleMode

    init {
        viewModelScope.launch(mainDispatcher) {
            _shuffleMode.value = mediaControllerAsync.await().shuffleModeEnabled
        }
        viewModelScope.launch {
            shuffleModeFlow.flow().collect {
                _shuffleMode.value = it
            }
        }
    }
}