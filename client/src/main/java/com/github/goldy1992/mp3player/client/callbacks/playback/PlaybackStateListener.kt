package com.github.goldy1992.mp3player.client.callbacks.playback

import android.support.v4.media.session.PlaybackStateCompat
import com.github.goldy1992.mp3player.client.callbacks.Listener

interface PlaybackStateListener : Listener{
    fun onPlaybackStateChanged(state: PlaybackStateCompat)
}