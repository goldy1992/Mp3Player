package com.github.goldy1992.mp3player.client.data

import com.github.goldy1992.mp3player.client.ui.states.State

data class Songs
    constructor(
        val state : State = State.NOT_LOADED,
        val songs: List<Song> = emptyList(),
        val totalDuration : Long = 0L
    ) : MediaEntity {
        companion object {
            val NOT_LOADED = Songs(State.NOT_LOADED)
            val LOADED = Songs(State.LOADED)
            val NO_RESULTS = Songs(State.NO_RESULTS)
        }
    }