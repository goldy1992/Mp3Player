package com.github.goldy1992.mp3player.client.callbacks

import com.github.goldy1992.mp3player.commons.LogTagger

abstract class Callback : LogTagger {
    private val listeners : MutableSet<Listener> = HashSet()

    open fun processCallback(data : Any) {
        for (listener in listeners) {
            updateListener(listener, data)
        }
    }

    abstract fun updateListener(listener: Listener, data: Any)

    open fun registerListener(listener : Listener) {
        listeners.add(listener)
    }

    open fun removeListener(listener : Listener) : Boolean {
        return listeners.remove(listener)
    }
}