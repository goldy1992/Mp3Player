package com.github.goldy1992.mp3player.client.utils

import android.os.SystemClock
import android.support.v4.media.session.PlaybackStateCompat
import java.text.SimpleDateFormat
import java.util.*

/**
 * Util Class for the timer
 */
object TimerUtils {
    /**
     * One second constant
     */
    const val ONE_SECOND = 1000
    private const val LOG_TAG = "TIMER_UTILS"
    @JvmStatic
    fun convertToSeconds(milliseconds: Long): Int {
        return (milliseconds / ONE_SECOND).toInt()
    }

    @JvmStatic
    fun formatTime(milliseconds: Long): String {
        val date = Date(milliseconds)
        val timerFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        //Log.d(LOG_TAG, "returning formatted time: " + formattedTime);
        return timerFormat.format(date)
    }

    @JvmStatic
    fun calculateCurrentPlaybackPosition(state: PlaybackStateCompat?): Long {
        if (state == null) {
            return 0L
        }
        return if (state.state != PlaybackStateCompat.STATE_PLAYING) {
            state.position
        } else {
            val timestamp = state.lastPositionUpdateTime ?: return state.bufferedPosition
            val currentTime = SystemClock.elapsedRealtime()
            val timeDiff = currentTime - timestamp
            state.position + timeDiff
        }
    }
}