package com.github.goldy1992.mp3player.commons

import android.support.v4.media.MediaMetadataCompat

object MetadataUtils {

    fun getDuration(metadata : MediaMetadataCompat?) : Long {
        return metadata?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION) ?: 0L
    }
}