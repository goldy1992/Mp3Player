package com.github.goldy1992.mp3player.client.ui.viewmodel.state

import android.util.Log
import androidx.media3.common.Player
import com.github.goldy1992.mp3player.client.data.RepeatMode
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
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
                    val repeatMode = when(it) {
                        Player.REPEAT_MODE_OFF -> RepeatMode.OFF
                        Player.REPEAT_MODE_ALL -> RepeatMode.ALL
                        Player.REPEAT_MODE_ONE -> RepeatMode.ONE
                        else -> RepeatMode.OFF
                    }
                    _repeatModeState.value = repeatMode
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