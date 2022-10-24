package com.github.goldy1992.mp3player.client.utils

import android.content.Context
import android.content.pm.PackageInfo
import com.github.goldy1992.mp3player.commons.Constants

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
    fun getAppVersion() : String {
        val pInfo: PackageInfo = context.packageManager
            .getPackageInfo(context.packageName, 0)
        return pInfo.versionName ?: Constants.UNKNOWN
    }
}