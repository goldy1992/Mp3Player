package com.github.goldy1992.mp3player.client.permissions

import android.app.Activity
import android.content.Context
import androidx.annotation.IntRange
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 * Wrapper class to simplify testing when using [ContextCompat].
 */
@ActivityScoped
class CompatWrapper
    @Inject
    constructor(@ActivityContext private val context: Context) {

    /**
     * Calls[ContextCompat.checkSelfPermission].
     * @return The result of [ContextCompat.checkSelfPermission].
     */
    fun checkPermissions(permission : String) :Int {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        )
    }

    /**
     * Requests permissions using [ActivityCompat.requestPermissions].
     */
    fun requestPermissions(permissions : Array<String>,
                           @IntRange(from = 0) requestCode : Int) {
        ActivityCompat.requestPermissions(
            context as Activity, permissions,
            requestCode
        )
    }
}