package com.github.goldy1992.mp3player.client.ui.viewmodel.state

import android.util.Log
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.ui.states.QueueState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QueueViewModelState (
    mediaRepository: MediaRepository,
    scope: CoroutineScope
) : MediaViewModelState<QueueState>(mediaRepository, scope) {

    private val _queueState = MutableStateFlow(QueueState.EMPTY)

    init {
        Log.v(logTag(), "init isPlaying")
        scope.launch {
            mediaRepository.queue()
                .collect {
                    Log.d(logTag(), "mediaRepository.isPlaying() collect: current isPlaying: $it")
                    _queueState.value = it
                }
        }
    }

    override fun state(): StateFlow<QueueState> {
        return _queueState.asStateFlow()
    }

    override fun logTag(): String {
        return "QueueViewModelState"
    }

}