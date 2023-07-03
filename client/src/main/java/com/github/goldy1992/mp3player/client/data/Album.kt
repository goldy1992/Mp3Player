package com.github.goldy1992.mp3player.client.data

import android.net.Uri
import com.github.goldy1992.mp3player.client.ui.states.State
import com.github.goldy1992.mp3player.commons.Constants

data class Album
    constructor(
        override val id : String = Constants.UNKNOWN,
        val albumTitle : String = Constants.UNKNOWN,
        val albumArtist : String = Constants.UNKNOWN,
        val recordingYear : String = Constants.UNKNOWN,
        val releaseYear : String = Constants.UNKNOWN,
        val playlist: Playlist = Playlist(State.NO_RESULTS),
        val totalDuration : Long = 0L,
        val albumArt : Uri = Uri.EMPTY,
        val state : State = State.NO_RESULTS
    ) : MediaEntity