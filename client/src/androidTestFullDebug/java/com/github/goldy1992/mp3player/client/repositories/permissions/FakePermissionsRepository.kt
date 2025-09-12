package com.github.goldy1992.mp3player.client.repositories.permissions

import com.github.goldy1992.mp3player.client.data.repositories.permissions.IPermissionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FakePermissionsRepository

@Inject
constructor() : IPermissionsRepository{
    val permissionsFlow : MutableStateFlow<Map<String, Boolean>> = MutableStateFlow(emptyMap())
    override fun permissionsFlow(): Flow<Map<String, Boolean>> {
        return permissionsFlow
    }

    override suspend fun setPermissions(permissionGrantedArray: Map<String, Boolean>) {
        permissionsFlow.value = permissionGrantedArray
    }

    override fun hasPlaybackPermissions(): Boolean {
        return true
    }

    override fun hasStorageReadPermissions(): Boolean {
        return true
    }

    override fun hasNotificationPermissions(): Boolean {
        return true
    }
}