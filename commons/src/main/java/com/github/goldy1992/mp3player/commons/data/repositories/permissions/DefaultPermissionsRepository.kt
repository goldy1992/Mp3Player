package com.github.goldy1992.mp3player.commons.data.repositories.permissions

import android.Manifest
import android.os.Build
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import com.github.goldy1992.mp3player.commons.VersionUtils.isTiramisuOrHigher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Keep
@Singleton
class DefaultPermissionsRepository

@Inject
constructor() : IPermissionsRepository {

    override suspend fun setPermissions(permissionGrantedArray: Map<String, Boolean>) {
        val newMap = HashMap<String, Boolean>()
        _permissionsState.value.entries.forEach { entry -> newMap[entry.key] = entry.value }
        permissionGrantedArray.entries.forEach { entry -> newMap[entry.key] = entry.value }
        _permissionsState.value = newMap
    }

    override fun hasPlaybackPermissions(): Boolean {
        if (isTiramisuOrHigher()) {
            return this._permissionsState.value[Manifest.permission.READ_MEDIA_AUDIO] ?: false
        }
        return true
    }

    override fun hasStorageReadPermissions(): Boolean {
        val currentPermissionsMap = this._permissionsState.value
        if (isTiramisuOrHigher()) {
            return hasStorageReadPermissionsTiramisu()
        }
        return currentPermissionsMap[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun hasStorageReadPermissionsTiramisu(): Boolean {
        val currentPermissionsMap = this._permissionsState.value
        return currentPermissionsMap.containsKey(Manifest.permission.READ_MEDIA_AUDIO) &&
                currentPermissionsMap[Manifest.permission.READ_MEDIA_AUDIO] ?: false &&
                currentPermissionsMap.containsKey(Manifest.permission.READ_MEDIA_IMAGES) &&
                currentPermissionsMap[Manifest.permission.READ_MEDIA_IMAGES] ?: false
    }

    override fun hasNotificationPermissions(): Boolean {
        if (isTiramisuOrHigher()) {
            return this._permissionsState.value[Manifest.permission.POST_NOTIFICATIONS] ?: false
        }
        return true

    }

    private val _permissionsState: MutableStateFlow<Map<String, Boolean>> = MutableStateFlow(
        emptyMap()
    )

    override fun permissionsFlow(): Flow<Map<String, Boolean>> {
        return _permissionsState.asStateFlow()
    }
}