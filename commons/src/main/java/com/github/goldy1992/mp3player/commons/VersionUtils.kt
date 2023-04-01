package com.github.goldy1992.mp3player.commons

import android.os.Build

object VersionUtils {

    fun isTiramisuOrHigher() : Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    fun isAndroid12OrHigher() : Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }
}