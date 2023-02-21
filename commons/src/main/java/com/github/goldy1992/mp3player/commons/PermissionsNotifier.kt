package com.github.goldy1992.mp3player.commons

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionsNotifier
    @Inject
    constructor() {

    private val _permissionsGranted = MutableStateFlow(false)
    val permissionsGranted : StateFlow<Boolean> = _permissionsGranted


    fun setPermissionGranted(granted : Boolean) {
        _permissionsGranted.value = granted
    }

}