package com.github.goldy1992.mp3player.commons

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionsNotifier
    @Inject
    constructor() {

    private val permissionsListeners = mutableSetOf<PermissionsListener>()

    fun addListener(permissionsListener: PermissionsListener) {
        this.permissionsListeners.add(permissionsListener)
    }

    fun removeListener(permissionsListener: PermissionsListener) {
        this.permissionsListeners.remove(permissionsListener)
    }

    fun notifyPermissionsGranted() {
        permissionsListeners.forEach { it.onPermissionsGranted() }
    }


}