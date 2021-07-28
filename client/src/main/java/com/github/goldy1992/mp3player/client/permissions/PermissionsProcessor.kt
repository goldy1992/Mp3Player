package com.github.goldy1992.mp3player.client.permissions

import android.Manifest.permission
import android.content.pm.PackageManager
import android.util.Log
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ActivityScoped
import java.util.*
import javax.inject.Inject

@ActivityScoped
class PermissionsProcessor
    @Inject
    constructor(private val permissionGranted: PermissionGranted,
                private val compatWrapper: CompatWrapper
    ): LogTagger {


    fun requestPermission(permission: String) { // Here, thisActivity is the current activity
        if (compatWrapper.checkPermissions(
                permission
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            compatWrapper.requestPermissions(
                arrayOf(permission),
                PERMISSION_RQ_CODE_MAP[permission]!!
            )
        } else { // Permission has already been granted
            Log.i(logTag(), "Permission has already been granted")
            permissionGranted.onPermissionGranted()
        }
    }

    companion object {
        private val PERMISSION_RQ_CODE_MAP: MutableMap<String, Int> = HashMap()
    }

    init {
        PERMISSION_RQ_CODE_MAP[permission.WRITE_EXTERNAL_STORAGE] = 0
    }

    override fun logTag(): String {
        return "PERMISSIONS_PROCESSOR"
    }


}