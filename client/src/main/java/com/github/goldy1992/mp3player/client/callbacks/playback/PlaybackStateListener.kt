package com.github.goldy1992.mp3player.client.callbacks.playback

import android.support.v4.media.session.PlaybackStateCompat

interface PlaybackStateListener {
    fun onPlaybackStateChanged(state: PlaybackStateCompat)
}