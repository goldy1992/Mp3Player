package com.github.goldy1992.mp3player.testdata

class SongBuilder {

    private var title : String? = null
    private var artist : String? = null

    fun title(title : String?) : SongBuilder {
        this.title = title
        return this
    }

    fun artist(artist : String?) : SongBuilder {
        this.artist = artist
        return this
    }

    fun build() : Song {
        return Song(title, artist)
    }
}