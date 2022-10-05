package com.github.goldy1992.mp3player.client

import androidx.media3.common.Player

data class PlayerEventHolder(
    val player: Player?,
    val events: Player.Events
)
