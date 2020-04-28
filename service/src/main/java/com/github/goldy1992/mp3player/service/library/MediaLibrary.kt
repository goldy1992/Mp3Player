package com.github.goldy1992.mp3player.service.library

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import javax.inject.Inject

@ComponentScope
class MediaLibrary @Inject constructor(private val contentManager: ContentManager) : LogTagger {

    fun getChildren(parentId: String): List<MediaBrowserCompat.MediaItem?>? {
        return contentManager.getChildren(parentId)
    }

    override fun logTag(): String {
        return "MEDIA_LIBRARY"
    }

}