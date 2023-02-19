package com.github.goldy1992.mp3player.client.utils

import android.util.Log
import com.github.goldy1992.mp3player.client.data.Song
import com.github.goldy1992.mp3player.commons.LogTagger
import org.apache.commons.lang3.StringUtils

object SongUtils : LogTagger {

    fun isSongItemSelected(song : Song, currentItem : Song) : Boolean {
        val isSelected = StringUtils.equals(song.id, currentItem.id)
        //Log.i(logTag(), "isSelected: $isSelected, songId: ${song.id}, currentItemId: ${currentItem.id}")
        return isSelected
    }

    override fun logTag(): String {
        return "SongUtils"
    }
}