package com.github.goldy1992.mp3player.client.callbacks.playback

import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.client.callbacks.Callback
import com.github.goldy1992.mp3player.client.callbacks.Listener
import javax.inject.Inject

class MyPlaybackStateCallback

    @Inject
    constructor() : Callback() {
    override fun updateListener(listener: Listener, data: Any) {
        (listener as PlaybackStateListener).onPlaybackStateChanged(data as PlaybackStateCompat)
    }

    override fun logTag(): String {
        return "MY_PLYBK_ST_CLLBK"
    }
}