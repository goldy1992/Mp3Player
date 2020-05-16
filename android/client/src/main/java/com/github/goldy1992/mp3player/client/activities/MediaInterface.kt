package com.github.goldy1992.mp3player.client.activities

import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.MediaBrowserConnectionListener
import com.github.goldy1992.mp3player.client.MediaControllerAdapter
import com.github.goldy1992.mp3player.client.callbacks.Listener
import com.github.goldy1992.mp3player.client.callbacks.connection.MyConnectionCallback
import com.github.goldy1992.mp3player.commons.DependencyInitialiser
import com.github.goldy1992.mp3player.commons.LogTagger

interface MediaInterface : DependencyInitialiser, MediaBrowserConnectionListener, LogTagger {

    /** MediaBrowserAdapter  */
    var mediaBrowserAdapter: MediaBrowserAdapter

    var myConnectionCallback : MyConnectionCallback

    var mediaControllerAdapter: MediaControllerAdapter



    fun mediaBrowserConnectionListeners() : Set<MediaBrowserConnectionListener>

    /**
     * @return A set of media controller listeners
     */
    fun mediaControllerListeners() : Set<Listener>

    fun connect() {
        myConnectionCallback.registerMediaControllerAdapter(mediaControllerAdapter)
        myConnectionCallback.registerListeners(mediaBrowserConnectionListeners())
        mediaControllerAdapter.registerListeners(mediaControllerListeners())
        mediaBrowserAdapter.connect()
    }

    fun disconnect() {
        mediaControllerAdapter.disconnect()
        mediaBrowserAdapter.disconnect()
    }

}