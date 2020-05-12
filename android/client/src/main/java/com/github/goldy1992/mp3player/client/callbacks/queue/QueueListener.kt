package com.github.goldy1992.mp3player.client.callbacks.queue

import android.support.v4.media.session.MediaSessionCompat
import com.github.goldy1992.mp3player.client.callbacks.Listener

interface QueueListener : Listener {

    fun onQueueChanged(queue: List<MediaSessionCompat.QueueItem>)
}