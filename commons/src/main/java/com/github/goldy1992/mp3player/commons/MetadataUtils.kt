package com.github.goldy1992.mp3player.commons

import android.support.v4.media.MediaMetadataCompat

object MetadataUtils {

    fun getDuration(metadata : MediaMetadataCompat?) : Long {
        return metadata?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION) ?: 0L
    }

    fun getMediaId(metadata : MediaMetadataCompat) : String {
        return metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID) ?: Constants.UNKNOWN
    }

    fun getLibraryId(metadata : MediaMetadataCompat) : String {
        return metadata.getString(Constants.LIBRARY_ID) ?: Constants.UNKNOWN
    }
}