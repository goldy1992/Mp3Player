package com.github.goldy1992.mp3player.client.ui.viewmodel.actions

import androidx.media3.common.Player
import com.github.goldy1992.mp3player.client.data.RepeatMode
import kotlinx.coroutines.launch

interface SetRepeatMode : MediaViewModelBase {

    fun setRepeatMode(repeatMode: RepeatMode) {
        scope.launch {
            val repeatModeInt = when(repeatMode) {
                RepeatMode.ONE -> Player.REPEAT_MODE_ONE
                RepeatMode.OFF -> Player.REPEAT_MODE_OFF
                RepeatMode.ALL -> Player.REPEAT_MODE_ALL
            }
            mediaRepository.setRepeatMode(repeatModeInt) }
    }
}