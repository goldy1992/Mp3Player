package com.github.goldy1992.mp3player.client.ui.viewmodel.state

import android.util.Log
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.models.RepeatMode
import com.github.goldy1992.mp3player.client.utils.RepeatModeUtils.getRepeatMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RepeatModeViewModelState (
    mediaRepository: MediaRepository,
    scope: CoroutineScope
) : MediaViewModelState<RepeatMode>(mediaRepository, scope) {

    private val _repeatModeState = MutableStateFlow(RepeatMode.OFF)

    init {
        Log.v(logTag(), "init")
        scope.launch {
            mediaRepository.repeatMode()
                .collect {
                    Log.d(logTag(), "state collect: $it")
                    _repeatModeState.value = it
                }
        }
    }

    override fun state(): StateFlow<RepeatMode> {
        return _repeatModeState.asStateFlow()
    }

    override fun logTag(): String {
        return "IsPlayingViewModelState"
    }

}