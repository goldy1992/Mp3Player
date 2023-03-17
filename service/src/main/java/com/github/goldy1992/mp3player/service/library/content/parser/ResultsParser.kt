package com.github.goldy1992.mp3player.service.library.content.parser

import android.database.Cursor
import android.os.Bundle
import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.Constants.MEDIA_ITEM_TYPE
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType

abstract class ResultsParser : Comparator<MediaItem>, LogTagger {

    abstract fun create(cursor: Cursor?) : List<MediaItem>

    abstract val type: MediaItemType?

    protected val extras: Bundle
        get() {
            val extras = Bundle()
            extras.putSerializable(MEDIA_ITEM_TYPE, type)
            return extras
        }
}