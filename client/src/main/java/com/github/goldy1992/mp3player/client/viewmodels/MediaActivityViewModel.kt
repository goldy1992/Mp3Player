package com.github.goldy1992.mp3player.client.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.commons.LogTagger

class MediaActivityViewModel : ViewModel(), MediaBrowserConnectionListener, LogTagger {

    // MediaBrowserConnectorCallback
    override fun onConnectionSuspended() { /* TODO: implement onConnectionSuspended */
        Log.i(logTag(), "connection suspended")
    }

    // MediaBrowserConnectorCallback
    override fun onConnectionFailed() { /* TODO: implement onConnectionFailed */
        Log.i(logTag(), "connection failed")
    }

    override fun logTag(): String {
        return "MediaActivityViewModel"
    }


}