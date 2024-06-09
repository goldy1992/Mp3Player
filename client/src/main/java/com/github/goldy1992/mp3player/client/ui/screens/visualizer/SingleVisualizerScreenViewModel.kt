package com.github.goldy1992.mp3player.client.ui.screens.visualizer

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.AudioDataProcessor
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.components.equalizer.VisualizerType
import com.github.goldy1992.mp3player.client.ui.viewmodel.MediaViewModel
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Pause
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.Play
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToNext
import com.github.goldy1992.mp3player.client.ui.viewmodel.actions.SkipToPrevious
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.AudioDataViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.CurrentSongViewModelState
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.IsPlayingViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
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
        val currentAudioData = currentAudioDataString.split(",").map { it.toFloat() }.toList()
        audioData.audioDataState.value = currentAudioData
    }
    override fun logTag(): String {
        return "SingleVisualizerScreenViewModel"
    }
}