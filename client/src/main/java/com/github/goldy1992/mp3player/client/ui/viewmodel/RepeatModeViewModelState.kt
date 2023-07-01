package com.github.goldy1992.mp3player.client.ui.viewmodel

import android.util.Log
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RepeatModeViewModelState (
    mediaRepository: MediaRepository,
    scope: CoroutineScope
) : MediaViewModelState<@Player.RepeatMode Int>(mediaRepository, scope) {

    private val _repeatModeState = MutableStateFlow(Player.REPEAT_MODE_OFF)

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

    override fun state(): StateFlow<@Player.RepeatMode Int> {
        return _repeatModeState.asStateFlow()
    }

    override fun logTag(): String {
        return "IsPlayingViewModelState"
    }

}