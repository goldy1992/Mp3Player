package com.github.goldy1992.mp3player.service.library.content.retrievers

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemType

/**
 * Represents the fundamentals of a Content Retriever
 */
abstract class ContentRetriever {
    abstract fun getItems() : List<MediaItem>

    abstract fun getChildren(parentId : String) : List<MediaItem>

    /**
     * @return The type of MediaItem retrieved from the Content Retriever
     */
    abstract val type: MediaItemType
}