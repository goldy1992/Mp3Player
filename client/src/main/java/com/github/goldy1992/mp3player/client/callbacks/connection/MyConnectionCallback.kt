package com.github.goldy1992.mp3player.client.callbacks.connection

import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

/**
 * Created by Mike on 04/10/2017.
 */
@ActivityRetainedScoped
class MyConnectionCallback

    @Inject
    constructor(private val connectionStatus: MutableLiveData<ConnectionStatus>)
    :  MediaBrowserCompat.ConnectionCallback() {


    override fun onConnected() {
        connectionStatus.postValue(ConnectionStatus.CONNECTED)
    }

    override fun onConnectionSuspended() {
        connectionStatus.postValue(ConnectionStatus.SUSPENDED)
    }

    override fun onConnectionFailed() {
        connectionStatus.postValue(ConnectionStatus.FAILED)
    }
}