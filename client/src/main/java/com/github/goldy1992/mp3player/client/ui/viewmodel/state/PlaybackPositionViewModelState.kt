package com.github.goldy1992.mp3player.client.ui.viewmodel.state

import android.util.Log
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.states.eventholders.PlaybackPositionEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaybackPositionViewModelState

constructor(mediaRepository: MediaRepository,
            scope: CoroutineScope
) : MediaViewModelState<PlaybackPositionEvent>(mediaRepository, scope) {

    private val _playbackPositionState = MutableStateFlow(PlaybackPositionEvent.DEFAULT)

    init {
        Log.v(logTag(), "init isPlaying")
        scope.launch {
            mediaRepository.playbackPosition()
                .collect {
                    Log.d(logTag(), "mediaRepository.isPlaying() collect: current isPlaying: $it")
                    _playbackPositionState.value = it
                }
        }
    }

    override fun state(): StateFlow<PlaybackPositionEvent> {
        return _playbackPositionState.asStateFlow()
    }

    override fun logTag(): String {
        return "PlaybackPositionViewModelState"
    }

}