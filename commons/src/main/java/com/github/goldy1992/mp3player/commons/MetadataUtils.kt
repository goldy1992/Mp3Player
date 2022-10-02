package com.github.goldy1992.mp3player.commons

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

object MetadataUtils {

    fun getDuration(metadata : MediaMetadata) : Long {
        return 0L
       // return metadata.d?.getLong(MediaMetadata.METADATA_KEY_DURATION) ?: 0L
    }

//    fun getMediaId(metadata : MediaMetadata) : String {
//        return metadata.id.getString(MediaMetadata.METADATA_KEY_MEDIA_ID) ?: Constants.UNKNOWN
//    }

    fun getLibraryId(metadata : MediaMetadata) : String {
        return metadata.extras?.getString(Constants.LIBRARY_ID) ?: Constants.UNKNOWN
    }
}