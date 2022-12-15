package com.github.goldy1992.mp3player.client.utils

import androidx.media3.common.Player
import androidx.media3.common.Player.*

object RepeatModeUtils {

    fun getNextRepeatMode(currentRepeatMode : @RepeatMode Int) : @RepeatMode Int {
        return when (currentRepeatMode) {
            REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
            REPEAT_MODE_ALL -> REPEAT_MODE_OFF
            REPEAT_MODE_OFF -> REPEAT_MODE_ONE
            else -> REPEAT_MODE_ONE
        }
    }
}