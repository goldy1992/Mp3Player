package com.github.goldy1992.mp3player.client.ui.viewmodel.state

import android.util.Log
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.models.PlaybackPositionEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaybackPositionViewModelState

constructor(mediaRepository: MediaRepository,
            scope: CoroutineScope
) : MediaViewModelState<PlaybackPositionEvent>(mediaRepository, scope) {

    companion object {
        const val LOG_TAG = "PlaybackPositionViewModelState"
    }
    private val _playbackPositionState = MutableStateFlow(PlaybackPositionEvent.DEFAULT)

    init {
        Log.v(LOG_TAG, "init isPlaying")
        scope.launch {
            mediaRepository.playbackPosition()
                .collect {
                    Log.d(LOG_TAG, "mediaRepository.isPlaying() collect: current isPlaying: $it")
                    _playbackPositionState.value = it
                }
        }
    }

    override fun state(): StateFlow<PlaybackPositionEvent> {
        return _playbackPositionState.asStateFlow()
    }


}