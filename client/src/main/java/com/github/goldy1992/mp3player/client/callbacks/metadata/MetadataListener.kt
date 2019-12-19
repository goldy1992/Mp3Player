package com.github.goldy1992.mp3player.client.callbacks.metadata

import android.support.v4.media.MediaMetadataCompat

interface MetadataListener {
    fun onMetadataChanged(metadata: MediaMetadataCompat)
}