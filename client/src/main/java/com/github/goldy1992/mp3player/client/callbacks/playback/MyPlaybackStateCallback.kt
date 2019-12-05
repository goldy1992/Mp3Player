package com.github.goldy1992.mp3player.client.callbacks.playback

import android.os.Handler
import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.client.callbacks.AsyncCallback
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class MyPlaybackStateCallback @Inject constructor(@Named("main") handler: Handler) : AsyncCallback<PlaybackStateCompat>(handler) {
    private val listeners: MutableSet<PlaybackStateListener>
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

    companion object {
        private const val LOG_TAG = "MY_PLYBK_ST_CLLBK"
    }

    init {
        listeners = HashSet()
    }
}