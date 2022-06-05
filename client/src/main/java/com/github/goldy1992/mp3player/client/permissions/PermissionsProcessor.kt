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


    fun requestPermissions(permissions: List<String>,
                           launcher : ActivityResultLauncher<Array<String>>) { // Here, thisActivity is the current activity
        val permissionsToRequest = arrayListOf<String>()
        var granted = true
        for (p in permissions) {
            if (compatWrapper.checkPermissions(p) != PackageManager.PERMISSION_GRANTED) {
                granted = false
                permissionsToRequest.add(p)
            }
        }
        if (!granted) {
            launcher.launch(permissionsToRequest.toTypedArray())
        } else { // Permission has already been granted
            Log.i(logTag(), "Permission has already been granted")
            permissionGranted.onPermissionGranted()
        }
    }

    override fun logTag(): String {
        return "PERMISSIONS_PROCESSOR"
    }


}