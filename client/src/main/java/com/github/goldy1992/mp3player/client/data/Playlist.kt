package com.github.goldy1992.mp3player.client.data

import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.Constants

/**
 * UI representation of a list of playable songs.
 */
data class Playlist
    constructor(
        val state : State = State.NOT_LOADED,
        val id : String = Constants.UNKNOWN,
        val songs: List<Song> = emptyList(),
        val totalDuration : Long = 0L
    ) : MediaEntity {
        companion object {
            val NOT_LOADED = Playlist(State.NOT_LOADED)
            val LOADED = Playlist(State.LOADED)
            val NO_RESULTS = Playlist(State.NO_RESULTS)
            val NO_PERMISSIONS = Playlist(State.NO_PERMISSIONS)
        }
    }