package com.github.goldy1992.mp3player.client.viewmodels;

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.goldy1992.mp3player.client.AudioDataProcessor
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.data.flows.player.AudioDataFlow
import com.github.goldy1992.mp3player.client.data.flows.player.IsPlayingFlow
import com.github.goldy1992.mp3player.client.viewmodels.states.IsPlaying
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MainDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    private val audioDataFlow: AudioDataFlow,
    @MainDispatcher val mainDispatcher: CoroutineDispatcher,
    private val isPlayingFlow: IsPlayingFlow) : LogTagger, ViewModel() {

    val isPlaying = IsPlaying.initialise(this, isPlayingFlow, mainDispatcher, mediaControllerAdapter.mediaControllerFuture)


    // Backing property to avoid state updates from other classes
    private val audioDataMutableState = MutableStateFlow(emptyList<Float>())
    // The UI collects from this StateFlow to get its state updates
    val audioDataState: StateFlow<List<Float>> = audioDataMutableState


    init {
        Log.i(logTag(), "creating viewmodel!")

        viewModelScope.launch {
            audioDataFlow.flow()
                .collect { audioData ->
                    if (isPlaying.state.value) {
                        Log.i(logTag(), "collecting audio data")
                        audioDataMutableState.value = audioDataProcessor.processAudioData(audioData).toList()
                        Log.i(logTag(), "finished collecting audio data")
                    }
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


    data class EqualizerState constructor(val isEqualizerActive : Boolean,
                                          val isActivityVisible : Boolean)

}
