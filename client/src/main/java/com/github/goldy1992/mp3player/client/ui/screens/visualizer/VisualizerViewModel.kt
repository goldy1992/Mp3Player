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
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.IsPlayingViewModelState
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [ViewModel] implementation for the [VisualizerScreen].
 */
@HiltViewModel
class VisualizerViewModel
@Inject
constructor(
    private val audioDataProcessor: AudioDataProcessor,
    override val mediaRepository: MediaRepository,
) : Pause, Play, SkipToNext, SkipToPrevious, ViewModel() {
    override val scope = viewModelScope
    val isPlaying = IsPlayingViewModelState(mediaRepository, viewModelScope)

    // Backing property to avoid state updates from other classes
    private val _audioData = MutableStateFlow(emptyList<Float>())
    // The UI collects from this StateFlow to get its state updates
    val audioData: StateFlow<List<Float>> = _audioData


    init {
        viewModelScope.launch {
            mediaRepository
                .audioData()
                .collect { audioData ->
                    Log.d(logTag(), "collecting audio data")
                    if (isPlaying.state().value) {
                        _audioData.value = audioDataProcessor.processAudioData(audioData, FrequencyBandTwentyFour()).toList()
                        Log.v(logTag(), "mediaRepository.audioData.collect() finished collecting audio data")
                    } else {
                        Log.v(logTag(), "mediaRepository.audioData.collect() not collecting audio data since song is not playing")
                    }
            }
        }

    }

    override fun logTag(): String {
        return "VisualizerViewModel"
    }

}
