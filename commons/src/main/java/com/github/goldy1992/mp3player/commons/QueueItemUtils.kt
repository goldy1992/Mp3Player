package com.github.goldy1992.mp3player.commons

import android.net.Uri
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat.QueueItem

object QueueItemUtils {

    private fun hasExtras(item: QueueItem?): Boolean {
        return item != null && item.description.extras != null
    }

    @JvmStatic
    fun getAlbumArtUri(item : QueueItem) : Uri? {
        val extras = item.description.extras
        return if (extras != null) {
            extras[MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI] as? Uri
        } else null
    }

}