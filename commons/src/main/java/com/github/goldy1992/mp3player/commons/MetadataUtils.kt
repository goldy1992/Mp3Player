package com.github.goldy1992.mp3player.commons

import androidx.media3.common.MediaMetadata

object MetadataUtils {

    fun getDuration(metadata : MediaMetadata) : Long {
        return metadata.extras?.getLong(MetaDataKeys.DURATION) ?: 0L
    }

}