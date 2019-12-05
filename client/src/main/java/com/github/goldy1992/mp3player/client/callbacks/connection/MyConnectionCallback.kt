package com.github.goldy1992.mp3player.client.callbacks.connection

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.MediaBrowserConnectorCallback
import javax.inject.Inject

/**
 * Created by Mike on 04/10/2017.
 */
class MyConnectionCallback @Inject constructor(private var mediaBrowserConnectorCallback: MediaBrowserConnectorCallback?) : MediaBrowserCompat.ConnectionCallback() {
    override fun onConnected() {
        if (null != mediaBrowserConnectorCallback) {
            mediaBrowserConnectorCallback!!.onConnected()
        }
    }

    override fun onConnectionSuspended() { // The Service has crashed. Disable transport controls until it automatically reconnects
    }

    override fun onConnectionFailed() { // The Service has refused our connection
    }

    fun setMediaBrowserConnectorCallback(mediaBrowserConnectorCallback: MediaBrowserConnectorCallback?) {
        this.mediaBrowserConnectorCallback = mediaBrowserConnectorCallback
    }

}