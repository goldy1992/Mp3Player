package com.github.goldy1992.mp3player.client.ui.screens;

import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.AudioDataAdapter
import com.github.goldy1992.mp3player.client.AudioDataProcessor
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VisualizerViewModel
    @Inject
    constructor(
        private val audioDataProcessor: AudioDataProcessor,
        val mediaControllerAdapter: MediaControllerAdapter,
        val audioDataAdapter: AudioDataAdapter) : LogTagger, ViewModel() {

    val equalizerPlayState : MediatorLiveData<EqualizerState> = MediatorLiveData()

    // Backing property to avoid state updates from other classes
    private val audioDataMutableState = MutableStateFlow(floatArrayOf())
    // The UI collects from this StateFlow to get its state updates
    val audioDataState: StateFlow<FloatArray> = audioDataMutableState

    // Backing property to avoid state updates from other classes
    private val isPlayingMutableState = MutableStateFlow(false)
    // The UI collects from this StateFlow to get its state updates
    val isPlaying: StateFlow<Boolean> = isPlayingMutableState

    init {
        Log.i(logTag(), "creating viewmodel!")

        viewModelScope.launch {
            audioDataAdapter.audioDataFlow
            .collect { audioData ->
                    Log.i(logTag(), "collecting audio data")
                    audioDataMutableState.value = audioDataProcessor.processAudioData(audioData)
                //    Log.i(logTag(), "finished collecting audio data")
                }
        }

        viewModelScope.launch {
            audioDataAdapter.isPlayingFlow
                .collect { playbackState ->
                    Log.i(logTag(), "collecting audio data")
                    isPlayingMutableState.value = playbackState.state == PlaybackStateCompat.STATE_PLAYING
                }
        }
    }

    override fun logTag(): String {
        return "VisualizerViewModel"
    }

    override fun onCleared() {
        Log.i(logTag(), "Calling on Cleared")
        super.onCleared()
    }

//    fun canProcessAudioData() : Boolean {
//        val result = isActivityVisible.value ?: false && isEqualizerActive.value ?: false
//        Log.i(logTag(), "isActivityVisible: ${isActivityVisible.value}, isEqualizerVisible: ${isActivityVisible.value}")
//        return result
//    }

    data class EqualizerState constructor(val isEqualizerActive : Boolean,
                        val isActivityVisible : Boolean)

}
