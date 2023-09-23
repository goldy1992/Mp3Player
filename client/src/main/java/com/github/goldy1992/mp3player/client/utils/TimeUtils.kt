package com.github.goldy1992.mp3player.client.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.pow

/**
 * Util Class for the timer
 */
object TimeUtils {
    /**
     * One second constant
     */
    private const val ONE_SECOND = 1000
    private const val logTag = "TIMER_UTILS"
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

    /**
     * From nano seconds (i.e. 10^-9)
     * Runtime speed is Deci-seconds (i.e. 10^-1)
     */
    fun convertNanoSecondsToRuntimeSpeed(valueNs: Long) : Float {
        return valueNs * 10.0.pow(-8.0).toFloat()
    }
}