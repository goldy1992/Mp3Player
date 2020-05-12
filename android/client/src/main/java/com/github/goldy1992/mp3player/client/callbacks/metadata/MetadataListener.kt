package com.github.goldy1992.mp3player.client.callbacks.metadata

import android.support.v4.media.MediaMetadataCompat
import com.github.goldy1992.mp3player.client.callbacks.Listener

interface MetadataListener : Listener {
    fun onMetadataChanged(metadata: MediaMetadataCompat)
}