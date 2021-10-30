package com.github.goldy1992.mp3player.service.library.content.parser

import android.database.Cursor
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat.MediaItem
import com.github.goldy1992.mp3player.commons.Constants.MEDIA_ITEM_TYPE
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType
import java.util.*

abstract class ResultsParser : Comparator<MediaItem>, LogTagger {

    abstract fun create(cursor: Cursor?, mediaIdPrefix: String?): List<MediaItem>

    abstract val type: MediaItemType?

    protected val extras: Bundle
        get() {
            val extras = Bundle()
            extras.putSerializable(MEDIA_ITEM_TYPE, type)
            return extras
        }
}