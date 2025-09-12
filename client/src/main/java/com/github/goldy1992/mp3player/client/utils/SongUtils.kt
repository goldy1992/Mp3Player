package com.github.goldy1992.mp3player.client.utils

import com.github.goldy1992.mp3player.client.models.media.Song
import org.apache.commons.lang3.StringUtils

object SongUtils {
    const val LOG_TAG = "SongUtils"

    fun isSongItemSelected(song: Song, currentItem: Song): Boolean {
        return StringUtils.equals(song.id, currentItem.id)
    }

}