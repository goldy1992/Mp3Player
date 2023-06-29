package com.github.goldy1992.mp3player.commons

import androidx.media3.common.FlagSet
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi


@UnstableApi
object PlayerUtils {

    fun buildPlayerEvents(@Player.Event vararg events : Int) : Player.Events {
        var flagSetBuilder = FlagSet.Builder()
        for (event in events) {
            flagSetBuilder = flagSetBuilder.add(event)
        }
        return Player.Events(flagSetBuilder.build())

    }

    fun defaultPlayerEvents() : Player.Events {
        return Player.Events(FlagSet.Builder().build())
    }
}