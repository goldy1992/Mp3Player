package com.github.goldy1992.mp3player.client.utils

import com.github.goldy1992.mp3player.client.data.RepeatMode
import com.github.goldy1992.mp3player.client.data.RepeatMode.ALL
import com.github.goldy1992.mp3player.client.data.RepeatMode.OFF
import com.github.goldy1992.mp3player.client.data.RepeatMode.ONE

object RepeatModeUtils {

    fun getNextRepeatMode(currentRepeatMode : RepeatMode) : RepeatMode {
        return when (currentRepeatMode) {
            ONE -> ALL
            ALL -> OFF
            OFF -> ONE
        }
    }
}