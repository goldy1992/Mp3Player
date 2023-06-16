package com.github.goldy1992.mp3player.client.utils

import android.content.Context
import com.github.goldy1992.mp3player.client.R
import com.github.goldy1992.mp3player.commons.MediaItemType

object MediaItemNameUtils {

    fun getMediaItemTypeName(context : Context, mediaItemType: MediaItemType) : String {
        return when (mediaItemType) {
            MediaItemType.ROOT -> context.getString(R.string.root)
            MediaItemType.ALBUMS -> context.getString(R.string.albums)
            MediaItemType.ALBUM -> context.getString(R.string.album)
            MediaItemType.SONGS -> context.getString(R.string.songs)
            MediaItemType.SONG -> context.getString(R.string.song)
            MediaItemType.FOLDERS -> context.getString(R.string.folders)
            MediaItemType.FOLDER -> context.getString(R.string.folder)
            else -> context.getString(R.string.none)
        }
    }
}