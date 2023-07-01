package com.github.goldy1992.mp3player.client.ui.viewmodel

import android.util.Log
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShuffleModeViewModelState (
    mediaRepository: MediaRepository,
    scope: CoroutineScope
) : MediaViewModelState<Boolean>(mediaRepository, scope) {

    private val _shuffleEnabledState = MutableStateFlow(false)

    init {
        Log.v(logTag(), "init")
        scope.launch {
            mediaRepository.isShuffleModeEnabled()
                .collect {
                    Log.d(logTag(), "collect: $it")
                    _shuffleEnabledState.value = it
                }
        }
    }

    override fun state(): StateFlow<Boolean> {
        return _shuffleEnabledState.asStateFlow()
    }

    override fun logTag(): String {
        return "ShuffleModeViewModelState"
    }

}