package com.github.goldy1992.mp3player.service.library

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class MediaLibrary @Inject constructor(private val contentManager: ContentManager) : LogTagger {

    fun getChildren(parentId: String): List<MediaBrowserCompat.MediaItem?>? {
        return contentManager.getChildren(parentId)
    }

    override fun logTag(): String {
        return "MEDIA_LIBRARY"
    }

}