package com.github.goldy1992.mp3player.client.data

import android.net.Uri
import com.github.goldy1992.mp3player.commons.Constants

data class Song
    constructor(
        val id : String = Constants.UNKNOWN,
        val title : String = Constants.UNKNOWN,
        val artist : String = Constants.UNKNOWN,
        val duration : Long = 0L,
        val albumArt : Uri = Uri.EMPTY
    ) : MediaEntity {
        companion object {
            val DEFAULT = Song()
        }
    }
