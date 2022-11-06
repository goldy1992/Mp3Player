package com.github.goldy1992.mp3player.commons

import android.os.SystemClock

object TimerUtils {

    fun getSystemTime() : Long {
        return SystemClock.elapsedRealtime()
    }
}