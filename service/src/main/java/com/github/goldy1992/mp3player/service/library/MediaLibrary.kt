package com.github.goldy1992.mp3player.service.library

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.LogTagger
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class MediaLibrary @Inject constructor(private val contentManager: ContentManager) : LogTagger {

    fun getChildren(parentId: String): List<MediaItem?> {
        return contentManager.getChildren(parentId)
    }

    override fun logTag(): String {
        return "MEDIA_LIBRARY"
    }

}