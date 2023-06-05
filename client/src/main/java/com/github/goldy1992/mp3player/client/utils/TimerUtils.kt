package com.github.goldy1992.mp3player.client.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Util Class for the timer
 */
object TimerUtils {
    /**
     * One second constant
     */
    private const val ONE_SECOND = 1000
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
}