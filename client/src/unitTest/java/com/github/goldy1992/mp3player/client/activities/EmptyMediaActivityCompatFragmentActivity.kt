package com.github.goldy1992.mp3player.client.activities

import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.callbacks.Listener

class EmptyMediaActivityCompatFragmentActivity : MediaActivityCompat() {


    override fun initialiseView(): Boolean {
        return true
    }

    override fun mediaBrowserConnectionListeners(): Set<MediaBrowserConnectionListener> {
        return emptySet()
    }

    override fun mediaControllerListeners(): Set<Listener> {
        return emptySet()
    }

    override fun logTag(): String {
        return "EMPTY_ACTIVITY"
    }
}