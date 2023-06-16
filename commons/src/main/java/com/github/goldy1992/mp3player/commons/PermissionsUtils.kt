package com.github.goldy1992.mp3player.commons

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

object PermissionsUtils : LogTagger {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val TIRAMISU_PERMISSIONS =  arrayOf(
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_IMAGES)

    private val STANDARD_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    fun getAppPermissions() : Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            TIRAMISU_PERMISSIONS
        } else {
            STANDARD_PERMISSIONS
        }
    }

    override fun logTag(): String {
        return "PermissionsUtils"
    }
}