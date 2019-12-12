package com.github.goldy1992.mp3player.client.callbacks.playback

import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.client.callbacks.AsyncCallback
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashSet

class MyPlaybackStateCallback

    @Inject
    constructor()
    : AsyncCallback<PlaybackStateCompat>() {


    private val listeners: MutableSet<PlaybackStateListener> = HashSet()
    override fun processCallback(data: PlaybackStateCompat) {
        for (listener in listeners) {
            listener.onPlaybackStateChanged(data)
        }
    }

    @Synchronized
    fun registerPlaybackStateListener(listener: PlaybackStateListener) {
        listeners.add(listener)
    }

    /**
     * @param listener the PlaybackStateListener to be removed
     * @return true if the listener was removed successfully
     */
    @Synchronized
    fun removePlaybackStateListener(listener: PlaybackStateListener?): Boolean {
        return listeners.remove(listener)
    }

    fun getListeners(): Set<PlaybackStateListener> {
        return listeners
    }

    override fun logTag(): String {
        return "MY_PLYBK_ST_CLLBK"
    }
}