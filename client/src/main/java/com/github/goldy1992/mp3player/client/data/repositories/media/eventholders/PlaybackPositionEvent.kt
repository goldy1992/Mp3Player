package com.github.goldy1992.mp3player.client.data.repositories.media.eventholders

import android.os.SystemClock

data class PlaybackPositionEvent(
    val isPlaying : Boolean,
    val currentPosition : Long,
    val systemTime : Long
) {
    companion object {
        val DEFAULT = PlaybackPositionEvent(false, 0L, SystemClock.elapsedRealtime())
    }
}
