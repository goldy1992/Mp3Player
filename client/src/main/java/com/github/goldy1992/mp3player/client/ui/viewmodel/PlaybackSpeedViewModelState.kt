package com.github.goldy1992.mp3player.client.ui.viewmodel

import android.util.Log
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaybackSpeedViewModelState (
    mediaRepository: MediaRepository,
    scope: CoroutineScope
) : MediaViewModelState<Float>(mediaRepository, scope) {

    private val _playbackSpeedState = MutableStateFlow(1f)

    init {
        Log.v(logTag(), "init isPlaying")
        scope.launch {
            mediaRepository.playbackSpeed()
                .collect {
                    Log.d(logTag(), "mediaRepository.isPlaying() collect: current isPlaying: $it")
                    _playbackSpeedState.value = it
                }
        }
    }

    override fun state(): StateFlow<Float> {
        return _playbackSpeedState.asStateFlow()
    }

    override fun logTag(): String {
        return "PlaybackSpeedViewModelState"
    }

}