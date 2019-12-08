package com.github.goldy1992.mp3player.service.library

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.content.retriever.ContentResolverRetriever
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaLibrary @Inject constructor(private val contentManager: ContentManager) {
    private val playlistRecursInSubDirectory = false
    private val contentRetrieverMap: Map<MediaItemType, ContentResolverRetriever>? = null
    private val LOG_TAG = "MEDIA_LIBRARY"
    fun getChildren(parentId: String): List<MediaBrowserCompat.MediaItem?>? {
        return contentManager.getChildren(parentId)
    }

    fun getMediaUriFromMediaId(mediaId: String?): Uri {
        throw UnsupportedOperationException()
    }

}