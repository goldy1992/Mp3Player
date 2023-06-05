package com.github.goldy1992.mp3player.client.ui.screens.visualizer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.AudioDataProcessor
import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBandTwentyFour
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.shareIn
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
    private val mediaRepository: MediaRepository,
) : LogTagger, ViewModel() {

    // isPlaying
    private val _isPlayingState = MutableStateFlow(false)
    val isPlaying : StateFlow<Boolean> = _isPlayingState

    init {
        viewModelScope.launch {
            mediaRepository
                .isPlaying()
                .collect {
                    _isPlayingState.value = it
                }
        }
    }

    // Backing property to avoid state updates from other classes
    private val _audioData = MutableStateFlow(emptyList<Float>())
    // The UI collects from this StateFlow to get its state updates
    val audioData: StateFlow<List<Float>> = _audioData


    init {
        viewModelScope.launch {
            mediaRepository.audioData()
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            )
            .collect { audioData ->
                if (isPlaying.value) {
                    Log.i(logTag(), "collecting audio data")
                    _audioData.value = audioDataProcessor.processAudioData(audioData, FrequencyBandTwentyFour()).toList()
                    Log.i(logTag(), "finished collecting audio data")
                }
            }
        }

    }

    fun play() {
        viewModelScope.launch { mediaRepository.play() }
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

    override fun logTag(): String {
        return "VisualizerViewModel"
    }

}
