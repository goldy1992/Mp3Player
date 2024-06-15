package com.github.goldy1992.mp3player.client.ui.screens.visualizer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.AudioDataProcessor
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.viewmodel.MediaViewModel
import com.github.goldy1992.mp3player.client.ui.viewmodel.state.AudioDataViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * [ViewModel] implementation for the [VisualizerCollectionScreen].
 */
@HiltViewModel
class VisualizerCollectionViewModel
@Inject
constructor(
    audioDataProcessor: AudioDataProcessor,
    mediaRepository: MediaRepository
) : MediaViewModel(mediaRepository) {
    val audioData = AudioDataViewModelState(mediaRepository, audioDataProcessor, playbackState, viewModelScope)

    override fun logTag(): String {
        return "VisualizerViewModel"
    }

}
