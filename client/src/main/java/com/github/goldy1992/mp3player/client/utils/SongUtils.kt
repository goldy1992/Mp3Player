package com.github.goldy1992.mp3player.client.utils

import com.github.goldy1992.mp3player.client.models.Song
import com.github.goldy1992.mp3player.commons.LogTagger
import org.apache.commons.lang3.StringUtils

object SongUtils : LogTagger {

    fun isSongItemSelected(song: Song, currentItem: Song): Boolean {
        return StringUtils.equals(song.id, currentItem.id)
    }

    override fun logTag(): String {
        return "SongUtils"
    }
}