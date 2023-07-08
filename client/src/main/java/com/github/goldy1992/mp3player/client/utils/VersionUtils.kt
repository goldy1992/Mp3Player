package com.github.goldy1992.mp3player.client.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.github.goldy1992.mp3player.commons.BuildConfig
import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.VersionUtils.isTiramisuOrHigher

/**
 * This Util class is used to get the version of the App from the [Context]. This can be mocked
 * during testing to simplify test dependencies.
 */
class VersionUtils

constructor(private val context: Context){

    /**
     * Gets the App version.
     * @return The version of the App or [Constants.UNKNOWN] if null.
     */
    @Suppress("DEPRECATION")
    fun getAppVersion() : String {
        val packageManager = context.packageManager
        val pInfo: PackageInfo = if (isTiramisuOrHigher()) {
            packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(context.packageName, 0)
        }
        val versionName = pInfo.versionName
        return if (versionName != null) {
            val versionNameSplit = versionName.split("-")
            if (BuildConfig.DEBUG) {
                "${versionNameSplit[0]}-DEBUG"
            } else {
                versionNameSplit[0]
            }

        } else {
            Constants.UNKNOWN
        }
    }
}