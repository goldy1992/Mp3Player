package com.github.goldy1992.mp3player.service.library.content.searcher

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemType

interface ContentSearcher {
    suspend fun search(query: String): List<MediaItem>?
    val searchCategory: MediaItemType?
}