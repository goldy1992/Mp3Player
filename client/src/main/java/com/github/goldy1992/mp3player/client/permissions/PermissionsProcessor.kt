package com.github.goldy1992.mp3player.client.permissions

import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PermissionsProcessor
    @Inject
    constructor(private val permissionGranted: PermissionGranted,
                private val compatWrapper: CompatWrapper
    ): LogTagger {

    var askedForPermissions = false


    fun requestPermission(permission: String, permissionLauncher : ActivityResultLauncher<String>) { // Here, thisActivity is the current activity
        if (compatWrapper.checkPermissions(permission) != PackageManager.PERMISSION_GRANTED) {
            askedForPermissions = true
            permissionLauncher.launch(permission)
        } else { // Permission has already been granted
            Log.i(logTag(), "Permission has already been granted")
            permissionGranted.onPermissionGranted()
        }
    }

    override fun logTag(): String {
        return "PERMISSIONS_PROCESSOR"
    }


}