package com.github.goldy1992.mp3player.client.ui.screens.visualizer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.AudioDataProcessor
import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBandTwentyFour
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Pause
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Play
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToNext
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToPrevious
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.AudioDataViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.CurrentSongViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.IsPlayingViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.PlaybackPositionViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.PlaybackSpeedViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] implementation for the [VisualizerCollectionScreen].
 */
@HiltViewModel
class VisualizerCollectionViewModel
@Inject
constructor(
    audioDataProcessor: AudioDataProcessor,
    override val mediaRepository: MediaRepository,
) : Pause, Play, SkipToNext, SkipToPrevious, ViewModel() {
    override val scope = viewModelScope

    val currentSong = CurrentSongViewModelState(mediaRepository, viewModelScope)
    val isPlaying = IsPlayingViewModelState(mediaRepository, viewModelScope)
    val audioData = AudioDataViewModelState(mediaRepository, audioDataProcessor, isPlaying.state(), scope)
    val playbackSpeed = PlaybackSpeedViewModelState(mediaRepository, viewModelScope)
    val playbackPosition = PlaybackPositionViewModelState(mediaRepository, viewModelScope)

    override fun logTag(): String {
        return "VisualizerViewModel"
    }

}
