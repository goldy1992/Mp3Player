package com.github.goldy1992.mp3player.client.ui.viewmodel.state

import android.util.Log
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.models.Queue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QueueViewModelState (
    mediaRepository: MediaRepository,
    scope: CoroutineScope
) : MediaViewModelState<Queue>(mediaRepository, scope) {
    companion object {
        const val LOG_TAG = "QueueViewModelState"
    }
    private val _queueState = MutableStateFlow(Queue.EMPTY)

    init {
        Log.v(LOG_TAG, "init isPlaying")
        scope.launch {
            mediaRepository.queue()
                .collect {
                    Log.d(LOG_TAG, "mediaRepository.isPlaying() collect: current isPlaying: $it")
                    _queueState.value = it
                }
        }
    }

    override fun state(): StateFlow<Queue> {
        return _queueState.asStateFlow()
    }

}