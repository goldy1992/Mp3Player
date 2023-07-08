package com.github.goldy1992.mp3player.client.data.repositories.media.eventholders

import android.os.SystemClock

data class OnPlaybackPositionChangedEvent(
    val isPlaying : Boolean,
    val currentPosition : Long,
    val systemTime : Long
) {
    companion object {
        val DEFAULT = OnPlaybackPositionChangedEvent(false, 0L, SystemClock.elapsedRealtime())
    }
}
