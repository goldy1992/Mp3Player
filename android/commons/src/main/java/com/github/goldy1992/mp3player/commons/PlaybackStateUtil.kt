package com.github.goldy1992.mp3player.commons

import android.support.v4.media.session.PlaybackStateCompat

object PlaybackStateUtil {
    @Deprecated("")
    fun getRepeatModeFromPlaybackStateCompat(playbackStateCompat: PlaybackStateCompat?): Int? {
        if (playbackStateCompat == null || playbackStateCompat.extras == null) {
            return null
        }
        val extras = playbackStateCompat.extras
        return extras!!.getInt(Constants.REPEAT_MODE)
    }
}