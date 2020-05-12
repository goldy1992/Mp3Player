package com.github.goldy1992.mp3player.client.callbacks.connection

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import io.flutter.plugin.common.EventChannel
import javax.inject.Inject

/**
 * Created by Mike on 04/10/2017.
 */
@ComponentScope
class MyConnectionCallback
    @Inject
    constructor()
    :  MediaBrowserCompat.ConnectionCallback(), EventChannel.EventSink{

    private var mediaControllerAdapter : MediaControllerAdapter? = null

    private val listeners : MutableSet<MediaBrowserConnectionListener> = HashSet()

    override fun onConnected() {
        mediaControllerAdapter?.onConnected()

        for (listener in listeners) {
            listener.onConnected()
        }
    }

    override fun onConnectionSuspended() {
        for (listener in listeners) {
            listener.onConnectionSuspended()
        }
        // The Service has crashed. Disable transport controls until it automatically reconnects
    }

    override fun onConnectionFailed() {
        for (listener in listeners) {
            listener.onConnectionFailed()
        }
        // The Service has refused our connection
    }

    fun registerListener(listener : MediaBrowserConnectionListener) {
        listeners.add(listener)
    }

    fun registerMediaControllerAdapter(mediaControllerAdapter: MediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter
    }

    fun registerListeners(listenerSet : Set<MediaBrowserConnectionListener>) {
        listeners.addAll(listenerSet)
    }

    fun removeListener(listener : MediaBrowserConnectionListener) : Boolean {
        return listeners.remove(listener)
    }

    override fun endOfStream() {
        TODO("Not yet implemented")
    }

    override fun error(errorCode: String?, errorMessage: String?, errorDetails: Any?) {
        TODO("Not yet implemented")
    }

    override fun success(event: Any?) {
        TODO("Not yet implemented")
    }
}