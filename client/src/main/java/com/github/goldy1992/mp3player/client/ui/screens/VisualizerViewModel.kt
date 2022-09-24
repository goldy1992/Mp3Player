package com.github.goldy1992.mp3player.client.ui.screens;

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.AudioDataProcessor
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.AudioSample
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VisualizerViewModel
    @Inject
    constructor(
        private val audioDataProcessor: AudioDataProcessor,
        val mediaControllerAdapter: MediaControllerAdapter) : LogTagger, ViewModel() {

    val equalizerPlayState : MediatorLiveData<EqualizerState> = MediatorLiveData()

    // Backing property to avoid state updates from other classes
    private val audioDataMutableState = MutableStateFlow(floatArrayOf())
    // The UI collects from this StateFlow to get its state updates
    val audioDataState: StateFlow<FloatArray> = audioDataMutableState

    init {
        Log.i(logTag(), "creating viewmodel!")

        viewModelScope.launch {
                mediaControllerAdapter.audioDataFlow()
                .collect { audioData ->
               //     Log.i(logTag(), "collecting audio data")
                    audioDataMutableState.value = audioDataProcessor.processAudioData(audioData)
                //    Log.i(logTag(), "finished collecting audio data")
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
