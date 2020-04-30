package com.github.goldy1992.mp3player.client

import com.github.goldy1992.mp3player.client.callbacks.Listener

/**
 * Defines a mechanism for a class to deal with changes to the MediaBrowserService Connection.
 */
interface MediaBrowserConnectionListener : Listener {
    /** Called when the component has successfully connected to the MediaBrowserService. */
    fun onConnected() {
        // Can be implemented if needed
    }
    /** Called when the connection to the MediaBrowserService has been suspended. */
    fun onConnectionSuspended() {
        // Can be implemented if needed
    }
    /** Called when the attempt to connect to the MediaBrowserService has failed. */
    fun onConnectionFailed() {
        // Can be implemented if needed
    }
}