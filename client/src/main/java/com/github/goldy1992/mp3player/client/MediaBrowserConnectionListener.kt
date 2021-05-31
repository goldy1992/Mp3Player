package com.github.goldy1992.mp3player.client

import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.callbacks.connection.ConnectionStatus

/**
 * Defines a mechanism for a class to deal with changes to the MediaBrowserService Connection.
 */
interface MediaBrowserConnectionListener : Listener {
    /** Called when the component has successfully connected to the MediaBrowserService. */
    fun onConnectionStatusChanged(connectionStatus: ConnectionStatus)
}