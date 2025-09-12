package com.github.goldy1992.mp3player.client.ui.viewmodel.state

import android.util.Log
import com.github.goldy1992.mp3player.client.AudioDataProcessor
import com.github.goldy1992.mp3player.client.data.audiobands.FrequencyBandTwentyFour
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.models.media.PlaybackState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AudioDataViewModelState(
    mediaRepository: MediaRepository,
    audioDataProcessor: AudioDataProcessor,
    playbackState: StateFlow<PlaybackState>,
    scope: CoroutineScope
) : MediaViewModelState<List<Float>>(mediaRepository, scope) {

    companion object {
        const val LOG_TAG = "AudioDataViewModelState"
    }

    // Expose this data to allow initialisation when initialising the visualizer when paused
     val audioDataState = MutableStateFlow(emptyList<Float>())


    init {
        Log.v(LOG_TAG, "init isPlaying")
        scope.launch {
            mediaRepository.audioData()
                .collect {audioSample ->
                    Log.d(LOG_TAG, "collecting audio data")
                    if (playbackState.value.isPlaying) {
                        audioDataState.value = audioDataProcessor.processAudioData(audioSample, FrequencyBandTwentyFour()).toList()
                        Log.v(LOG_TAG, "mediaRepository.audioData.collect() finished collecting audio data")
                    } else {
                        Log.v(LOG_TAG, "mediaRepository.audioData.collect() not collecting audio data since song is not playing")
                    }
                }
        }
    }

    override fun state(): StateFlow<List<Float>> {
        return audioDataState.asStateFlow()
    }

}