package com.github.goldy1992.mp3player.client.models

import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType

/**
 * UI representation of a list of playable songs.
 */
data class Playlist
    constructor(
        override val id : String = Constants.UNKNOWN,
        val state : State = State.NOT_LOADED,
        val songs: List<Song> = emptyList(),
        val totalDuration : Long = 0L
    ) : MediaEntity {
    override val type: MediaItemType = MediaItemType.SONGS
        companion object {
            val NOT_LOADED = Playlist(state= State.NOT_LOADED)
            val LOADED = Playlist(state= State.LOADED)
            val NO_RESULTS = Playlist(state= State.NO_RESULTS)
            val NO_PERMISSIONS = Playlist(state= State.NO_PERMISSIONS)
        }
    }