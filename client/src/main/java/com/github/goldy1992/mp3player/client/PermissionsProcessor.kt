package com.github.goldy1992.mp3player.client

import android.Manifest.permission
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.goldy1992.mp3player.client.activities.SplashScreenEntryActivity
import java.util.*
import javax.inject.Inject

class PermissionsProcessor

    @Inject
    constructor(private val parentActivity: SplashScreenEntryActivity,
                private val permissionGranted: PermissionGranted) {


    fun requestPermission(permission: String) { // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(parentActivity,
                        permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(parentActivity, arrayOf(permission), PERMISSION_RQ_CODE_MAP[permission]!!)
        } else { // Permission has already been granted
            Log.i(LOG_TAG, "Permission has already been granted")
            permissionGranted.onPermissionGranted()
        }
    }

    fun getPermissionFromRequestCode(requestCode: Int): String? {
        for (permission in PERMISSION_RQ_CODE_MAP.keys) {
            if (PERMISSION_RQ_CODE_MAP[permission] == requestCode) {
                return permission
            }
        }
        return null
    }

    companion object {
        private val PERMISSION_RQ_CODE_MAP: MutableMap<String, Int> = HashMap()
        private const val LOG_TAG = "PERMISSIONS_PROCESSOR"
    }

    init {
        PERMISSION_RQ_CODE_MAP[permission.WRITE_EXTERNAL_STORAGE] = 0
    }


}