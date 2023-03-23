package com.github.goldy1992.mp3player.commons.data.repositories.permissions

import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.flow.Flow

interface IPermissionsRepository {
    fun permissionsFlow() : Flow<Map<String, Boolean>>
    suspend fun setPermissions(permissionGrantedArray : Map<String, Boolean>)

    fun getPermissionsLauncher() : ActivityResultLauncher<Array<String>>?
    fun setPermissionsLauncher(launcher : ActivityResultLauncher<Array<String>>)

    fun hasPlaybackPermissions() : Boolean

    fun hasStorageReadPermissions() : Boolean

    fun hasNotificationPermissions() : Boolean


}