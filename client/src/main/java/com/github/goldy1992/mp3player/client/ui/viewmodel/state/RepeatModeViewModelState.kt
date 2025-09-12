package com.github.goldy1992.mp3player.client.ui.viewmodel.state

import android.util.Log
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.models.RepeatMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RepeatModeViewModelState (
    mediaRepository: MediaRepository,
    scope: CoroutineScope
) : MediaViewModelState<RepeatMode>(mediaRepository, scope) {
    companion object {
        const val LOG_TAG = "RepeatModeViewModelState"
    }
    private val _repeatModeState = MutableStateFlow(RepeatMode.OFF)

    init {
        Log.v(LOG_TAG, "init")
        scope.launch {
            mediaRepository.repeatMode()
                .collect {
                    Log.d(LOG_TAG, "state collect: $it")
                    _repeatModeState.value = it
                }
        }
    }

    override fun state(): StateFlow<RepeatMode> {
        return _repeatModeState.asStateFlow()
    }

}