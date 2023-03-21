package com.github.goldy1992.mp3player.commons.data.repositories.permissions

import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPermissionsRepository

@Inject
constructor() : IPermissionsRepository {

    override suspend fun setPermissions(permissionGrantedArray: Map<String, Boolean>) {
        _permissionsState.value = permissionGrantedArray
    }

    private var permissionsLauncher: ActivityResultLauncher<Array<String>>? = null
    override fun getPermissionsLauncher(): ActivityResultLauncher<Array<String>>? {
        return permissionsLauncher
    }

    override fun setPermissionsLauncher(launcher: ActivityResultLauncher<Array<String>>) {
        this.permissionsLauncher = launcher
    }

    private val _permissionsState: MutableStateFlow<Map<String, Boolean>> = MutableStateFlow(
        emptyMap()
    )

    override fun permissionsFlow(): Flow<Map<String, Boolean>> {
        return _permissionsState.asStateFlow()
    }
}