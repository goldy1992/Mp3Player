package com.github.goldy1992.mp3player.client.models

data class PlaybackParametersEvent(
    val speed : Float = 1.0f,
    val pitch : Float = 1.0f
) {
    companion object {
        val DEFAULT = PlaybackParametersEvent()
    }
}