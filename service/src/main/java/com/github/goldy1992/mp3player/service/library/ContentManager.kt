package com.github.goldy1992.mp3player.service.library

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType

/**
 * Abstraction of a Content Manager. An implementation should organise a collection of [MediaItem]s.
 */
interface ContentManager : LogTagger {

    suspend fun initialise(rootMediaItem: MediaItem)

    suspend fun getChildren(parentId : String) : List<MediaItem>

    suspend fun getChildren(mediaItemType: MediaItemType) : List<MediaItem>

    suspend fun getContentById(id : String) : MediaItem
    /**
     * @param query the search query
     * @return a [List] of [MediaItem]s that match the search query
     */
    suspend fun search(query : String) : List<MediaItem>
}