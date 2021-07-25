package com.github.goldy1992.mp3player.client

import android.Manifest.permission
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import java.util.*
import javax.inject.Inject

@ActivityScoped
class PermissionsProcessor
    @Inject
    constructor(@ActivityContext private val context : Context,
                private val permissionGranted: PermissionGranted) {


    fun requestPermission(permission: String) { // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                        permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(permission), PERMISSION_RQ_CODE_MAP[permission]!!)
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