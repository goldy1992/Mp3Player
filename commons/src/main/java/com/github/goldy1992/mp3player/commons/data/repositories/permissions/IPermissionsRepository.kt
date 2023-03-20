package com.github.goldy1992.mp3player.commons.data.repositories.permissions

import kotlinx.coroutines.flow.Flow

interface IPermissionsRepository {

    suspend fun setHasExternalStoragePermissions(hasPermissions : Boolean)
    fun hasExternalStoragePermissionFlow() : Flow<Boolean>

    suspend fun setNotificationPermissions(hasPermissions: Boolean)
    fun hasNotificationPermissions() : Flow<Boolean>

    suspend fun setAudioPermissions(hasPermissions: Boolean)
    fun hasAudioPermissions() : Flow<Boolean>

    suspend fun setPhotoPermissions(hasPermissions: Boolean)
    fun hasPhotoPermissions() : Flow<Boolean>

    fun hasAllPermissions() : Flow<Boolean>
}