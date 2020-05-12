package com.github.goldy1992.mp3player.service.library.content.searcher

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemType

interface ContentSearcher {
    fun search(query: String): List<MediaBrowserCompat.MediaItem>?
    val searchCategory: MediaItemType?
}