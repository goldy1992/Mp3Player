package com.github.goldy1992.mp3player.client.data

data class PlaybackParameters(
    val speed : Float = 1.0f,
    val pitch : Float = 1.0f
) {
    companion object {
        val DEFAULT = PlaybackParameters()
    }
}