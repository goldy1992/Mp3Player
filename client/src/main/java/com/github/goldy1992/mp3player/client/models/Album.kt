package com.github.goldy1992.mp3player.client.models

import android.net.Uri
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType

data class Album
    constructor(
        override val id : String = Constants.UNKNOWN,
        val title : String = Constants.UNKNOWN,
        val artist : String = Constants.UNKNOWN,
        val recordingYear : String = Constants.UNKNOWN,
        val releaseYear : String = Constants.UNKNOWN,
        val playlist: Playlist = Playlist(state= State.NO_RESULTS),
        val artworkUri : Uri = Uri.EMPTY,
        val state : State = State.NO_RESULTS
    ) : MediaEntity {
    override val type: MediaItemType = MediaItemType.ALBUM
}