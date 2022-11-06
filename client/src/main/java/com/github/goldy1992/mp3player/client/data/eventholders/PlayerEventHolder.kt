package com.github.goldy1992.mp3player.client.data.eventholders

import androidx.media3.common.FlagSet
import androidx.media3.common.Player
import androidx.media3.common.Player.Events

data class PlayerEventHolder(
    val player: Player?,
    val events: Events
) {
    companion object{
        val EMPTY = PlayerEventHolder(null, Events(FlagSet.Builder().build()))
    }
}


