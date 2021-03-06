package com.github.goldy1992.mp3player.service.library.content.retriever

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.request.ContentRequest

/**
 * Represents the fundamentals of a Content Retriever
 */
abstract class ContentRetriever {
    /**
     * @param request the content request
     * @return a list of media items that match the content request
     */
    abstract fun getChildren(request: ContentRequest): List<MediaBrowserCompat.MediaItem>?

    /**
     * @return The type of MediaItem retrieved from the Content Retriever
     */
    abstract val type: MediaItemType
}