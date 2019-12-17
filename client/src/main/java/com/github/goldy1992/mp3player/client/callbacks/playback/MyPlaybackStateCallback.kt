package com.github.goldy1992.mp3player.client.callbacks.playback

import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.commons.LogTagger
import javax.inject.Inject

class MyPlaybackStateCallback

    @Inject
    constructor() : LogTagger {

    private val listeners: MutableSet<PlaybackStateListener> = HashSet()

    fun processCallback(data: PlaybackStateCompat) {
        for (listener in listeners) {
            listener.onPlaybackStateChanged(data)
        }
    }


    fun registerPlaybackStateListener(listener: PlaybackStateListener) {
        listeners.add(listener)
    }

    /**
     * @param listener the PlaybackStateListener to be removed
     * @return true if the listener was removed successfully
     */
    fun removePlaybackStateListener(listener: PlaybackStateListener?): Boolean {
        return listeners.remove(listener)
    }


    override fun logTag(): String {
        return "MY_PLYBK_ST_CLLBK"
    }
}