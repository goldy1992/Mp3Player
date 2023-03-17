package com.github.goldy1992.mp3player.service.library

import androidx.media3.common.MediaItem
import androidx.media3.session.MediaLibraryService.MediaLibrarySession
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType
import kotlinx.coroutines.flow.StateFlow

/**
 * Abstraction of a Content Manager. An implementation should organise a collection of [MediaItem]s.
 */
interface ContentManager : LogTagger {

    val isInitialised : StateFlow<Boolean>

    var mediaSession : MediaLibrarySession?

    suspend fun getChildren(parentId : String) : List<MediaItem>

    suspend fun getChildren(mediaItemType: MediaItemType) : List<MediaItem>

    suspend fun getContentById(id : String) : MediaItem

    suspend fun getContentByIds(ids : List<String>) : List<MediaItem>
    /**
     * @param query the search query
     * @return a [List] of [MediaItem]s that match the search query
     */
    suspend fun search(query : String) : List<MediaItem>
}