package com.github.goldy1992.mp3player.client

interface MediaBrowserConnectorCallback {
    fun onConnected()
    fun onConnectionSuspended()
    fun onConnectionFailed()
}