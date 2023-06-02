package com.github.goldy1992.mp3player.commons

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

object VersionUtils {

    @ChecksSdkIntAtLeast(api=Build.VERSION_CODES.TIRAMISU)
    fun isTiramisuOrHigher() : Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
    fun isAndroid12OrHigher() : Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    }
}