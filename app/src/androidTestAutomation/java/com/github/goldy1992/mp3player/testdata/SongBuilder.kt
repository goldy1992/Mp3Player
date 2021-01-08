package com.github.goldy1992.mp3player.testdata

import com.github.goldy1992.mp3player.commons.Constants

class SongBuilder {

    private var title : String = Constants.UNKNOWN
    private var artist : String = Constants.UNKNOWN

    fun title(title : String) : SongBuilder {
        this.title = title
        return this
    }

    fun artist(artist : String) : SongBuilder {
        this.artist = artist
        return this
    }

    fun build() : Song {
        return Song(title, artist)
    }
}