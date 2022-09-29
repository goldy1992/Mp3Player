package com.github.goldy1992.mp3player.client

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaControllerCompat.Callback
import android.support.v4.media.session.MediaSessionCompat
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject


@ActivityRetainedScoped
class ClientMediaController

    @Inject
    constructor(
        private val context: Context,
        private val mediaBrowserCompat: MediaBrowserCompat)  {

    var mediaController : MediaControllerCompat? = null

    fun getTransportControls() : MediaControllerCompat.TransportControls? {
        return mediaController?.transportControls
    }

    val callbacks : MutableSet<Callback> = mutableSetOf()

    override fun onConnected() {
        this.mediaController = MediaControllerCompat(context, mediaBrowserCompat.sessionToken)
        for (callback in callbacks) {
            this.mediaController.registerCallback(callback)
        }
    }

    fun registerCallback(callback: Callback) {
        callbacks.add(callback)
    }

    fun disconnect() {
        for (callback in callbacks) {
            mediaController?.unregisterCallback(callback)
        }
    }

    val connectedFlow : Flow<Boolean> = callbackFlow<Boolean> {

        val connectionListener = object : MediaBrowserConnectionListener
    }
}