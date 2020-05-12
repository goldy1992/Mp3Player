package com.github.goldy1992.mp3player.client.callbacks.queue

import android.support.v4.media.session.MediaSessionCompat
import com.github.goldy1992.mp3player.client.callbacks.Callback
import com.github.goldy1992.mp3player.client.callbacks.Listener
import javax.inject.Inject

class MyQueueCallback
    @Inject
    constructor() : Callback() {

    override fun updateListener(listener: Listener, data: Any) {
        (listener as QueueListener).onQueueChanged(data as List<MediaSessionCompat.QueueItem>)
    }

    override fun logTag(): String {
        return "MY_QUEUE_CALLBACK"
    }

}