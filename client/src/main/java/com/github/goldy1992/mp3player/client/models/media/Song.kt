package com.github.goldy1992.mp3player.client.models.media

import android.net.Uri
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType

data class Song
    constructor(
        override val id : String = Constants.UNKNOWN,
        val title : String = Constants.UNKNOWN,
        val artist : String = Constants.UNKNOWN,
        val duration : Long = 1000L,
        val albumArt : Uri = Uri.EMPTY,
        override val state: State = State.NOT_LOADED
    ) : MediaEntity {
    companion object {
        val DEFAULT = Song()
    }

    override val type: MediaItemType = MediaItemType.SONG
}
