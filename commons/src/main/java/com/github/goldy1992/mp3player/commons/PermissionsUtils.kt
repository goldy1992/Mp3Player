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

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private val UPSIDE_DOWN_CAKE_PERMISSIONS =  arrayOf(
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.READ_MEDIA_AUDIO,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK)


    private val STANDARD_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    fun getAppPermissions() : Array<String> {
        return when(Build.VERSION.SDK_INT) {
                Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> UPSIDE_DOWN_CAKE_PERMISSIONS
                Build.VERSION_CODES.TIRAMISU -> TIRAMISU_PERMISSIONS
                else -> STANDARD_PERMISSIONS
            }

    }

    override fun logTag(): String {
        return "PermissionsUtils"
    }
}