package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope

class EmptyMediaActivityCompatFragmentActivity : MediaActivityCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initialiseDependencies()
        super.onCreate(savedInstanceState)
    }

    override fun initialiseDependencies() {
        super.initialiseDependencies()
        this.mediaActivityCompatComponent.inject(this)
    }

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