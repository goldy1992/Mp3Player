package com.github.goldy1992.mp3player.client.utils

import androidx.media3.common.Player
import com.github.goldy1992.mp3player.client.models.RepeatMode
import com.github.goldy1992.mp3player.client.models.RepeatMode.ALL
import com.github.goldy1992.mp3player.client.models.RepeatMode.OFF
import com.github.goldy1992.mp3player.client.models.RepeatMode.ONE

object RepeatModeUtils {

    fun getNextRepeatMode(currentRepeatMode : RepeatMode) : RepeatMode {
        return when (currentRepeatMode) {
            ONE -> ALL
            ALL -> OFF
            OFF -> ONE
        }
    }

    private val playerToUiMap = mapOf<@Player.RepeatMode Int, RepeatMode>(
        Player.REPEAT_MODE_ALL to ALL,
        Player.REPEAT_MODE_ONE to ONE,
        Player.REPEAT_MODE_OFF to OFF
    ).withDefault { OFF }

    fun getRepeatMode(@Player.RepeatMode repeatMode : Int) : RepeatMode {
        return playerToUiMap[repeatMode] ?: OFF
    }

    fun getRepeatMode(repeatMode : RepeatMode) : Int {
        return uiToPlayerMap[repeatMode] ?: Player.REPEAT_MODE_OFF
    }

    val uiToPlayerMap : Map<RepeatMode, Int> = mapOf<RepeatMode, @Player.RepeatMode Int,>(
        ALL to Player.REPEAT_MODE_ALL,
        ONE to Player.REPEAT_MODE_ONE,
        OFF to Player.REPEAT_MODE_OFF
    ).withDefault { Player.REPEAT_MODE_OFF }
}