package com.github.goldy1992.mp3player.client.callbacks

import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener
import com.github.goldy1992.mp3player.client.callbacks.metadata.MyMetadataCallback
import com.github.goldy1992.mp3player.client.callbacks.playback.MyPlaybackStateCallback
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener
import com.github.goldy1992.mp3player.client.callbacks.queue.MyQueueCallback
import com.github.goldy1992.mp3player.client.callbacks.queue.QueueListener
import javax.inject.Inject

/**
 * Created by Mike on 04/10/2017.
 * TODO: ORGANIZE LISTENERS INTO CATEGORIES DEFINED BY THE ACTION THAT SHOULD BE SET IN THE ACTIONS LIST
 */
open class MyMediaControllerCallback

    @Inject
    constructor(val myMetaDataCallback: MyMetadataCallback,
                val myPlaybackStateCallback: MyPlaybackStateCallback,
                val myQueueCallback : MyQueueCallback)
    : MediaControllerCompat.Callback() {

    override fun onMetadataChanged(metadata: MediaMetadataCompat) {
        myMetaDataCallback.processCallback(metadata)
    }

    override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
        myPlaybackStateCallback.processCallback(state)
    }

    override fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) {
        myQueueCallback.processCallback(queue!!)
    }
    /**
     * Registers a callback listener.
     * @param listener The listener to be registered.
     */
    fun registerListener(listener: Listener) {
        if (listener is MetadataListener) {
            this.myMetaDataCallback.registerListener(listener)
        }

        if (listener is PlaybackStateListener) {
            this.myPlaybackStateCallback.registerListener(listener)
        }

        if (listener is QueueListener) {
            this.myQueueCallback.registerListener(listener)
        }
    }
    fun registerListeners(listeners: Collection<Listener>) {
        for (listener in listeners) {
            registerListener(listener)
        }
    }
    /**
     * Removes a callback listener.
     * @param listener The listener to be removed.
     */
    fun removeListener(listener: Listener) {
        if (listener is MetadataListener) {
            this.myMetaDataCallback.removeListener(listener)
        }

        if (listener is PlaybackStateListener) {
            this.myPlaybackStateCallback.removeListener(listener)
        }

        if (listener is QueueListener) {
            this.myQueueCallback.removeListener(listener)
        }
    }
}