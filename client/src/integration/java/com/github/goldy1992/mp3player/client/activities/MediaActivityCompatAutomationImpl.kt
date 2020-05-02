package com.github.goldy1992.mp3player.client.activities

import android.os.Bundle
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.client.callbacks.Listener
import java.util.*

class MediaActivityCompatAutomationImpl : MediaActivityCompat() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty)
    }

    override fun initialiseView(): Boolean {
        setContentView(R.layout.activity_empty)
        return true
    }

    override fun mediaBrowserConnectionListeners(): Set<MediaBrowserConnectionListener> {
        return Collections.emptySet()
    }

    override fun mediaControllerListeners(): Set<Listener> {
        return Collections.emptySet()
    }

    override fun logTag(): String {
        return ""
    }

    override fun initialiseDependencies() {
        super.initialiseDependencies()
        this.mediaActivityCompatComponent.inject(this)
    }
}