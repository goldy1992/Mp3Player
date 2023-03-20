package com.github.goldy1992.mp3player.commons.data.repositories.permissions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPermissionsRepository

@Inject
constructor() : IPermissionsRepository {

    private val _hasExternalStorageAccessState = MutableStateFlow(false)
    private val hasExternalStorageAccessState : StateFlow<Boolean>  = _hasExternalStorageAccessState
    override fun hasExternalStoragePermissionFlow(): Flow<Boolean> {
        return hasExternalStorageAccessState
    }

    override suspend fun setHasExternalStoragePermissions(hasPermissions: Boolean) {
        this._hasExternalStorageAccessState.value = hasPermissions
    }

    override fun hasAllPermissions(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    private val _hasNotificationPermissionsState = MutableStateFlow(false)
    private val hasNotificationPermissionsState : StateFlow<Boolean>  = _hasNotificationPermissionsState
    override fun hasNotificationPermissions(): Flow<Boolean> {
        return hasNotificationPermissionsState
    }
    override suspend fun setNotificationPermissions(hasPermissions: Boolean) {
        this._hasNotificationPermissionsState.value = hasPermissions
    }


    private val _audioPermissionsState = MutableStateFlow(false)
    private val audioPermissionsState : StateFlow<Boolean>  = _audioPermissionsState
    override fun hasAudioPermissions(): Flow<Boolean> {
       return audioPermissionsState
    }
    override suspend fun setAudioPermissions(hasPermissions: Boolean) {
        this._audioPermissionsState.value = hasPermissions
    }

    private val _photoPermissionsState = MutableStateFlow(false)
    private val photoPermissionsState : StateFlow<Boolean>  = _photoPermissionsState
    override fun hasPhotoPermissions(): Flow<Boolean> {
        return photoPermissionsState
    }
    override suspend fun setPhotoPermissions(hasPermissions: Boolean) {
        this._photoPermissionsState.value = hasPermissions
    }
}