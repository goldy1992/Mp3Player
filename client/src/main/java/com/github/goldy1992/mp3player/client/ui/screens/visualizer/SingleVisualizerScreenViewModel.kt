package com.github.goldy1992.mp3player.client.ui.screens.visualizer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.AudioDataProcessor
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.components.equalizer.VisualizerType
import com.github.goldy1992.mp3player.client.ui.viewmodel.MediaViewModel
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.AudioDataViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingleVisualizerScreenViewModel

@Inject
constructor(
    savedStateHandle: SavedStateHandle,
    audioDataProcessor: AudioDataProcessor,
    mediaRepository: MediaRepository,
) : MediaViewModel(mediaRepository) {

    val audioData = AudioDataViewModelState(mediaRepository, audioDataProcessor,  playbackState, viewModelScope)
    val visualizer : VisualizerType
    init {
        val visualiserTypeString :String = checkNotNull(savedStateHandle["visualizer"])
        visualizer = VisualizerType.valueOf(visualiserTypeString)
        val currentAudioDataString : String = checkNotNull(savedStateHandle["audioData"])
        if ("empty" != currentAudioDataString) {
            val currentAudioData = currentAudioDataString.split(",").map { it.toFloat() }.toList()
            audioData.audioDataState.value = currentAudioData
        }
    }
}