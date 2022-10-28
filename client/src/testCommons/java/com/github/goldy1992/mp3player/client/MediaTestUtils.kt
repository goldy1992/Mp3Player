package com.github.goldy1992.mp3player.client

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.session.MediaLibraryService

object MediaTestUtils {

    @JvmStatic
    fun createTestMediaMetaData() : MediaMetadata {
        return MediaMetadata
            .Builder()
            .setFolderType(MediaMetadata.FOLDER_TYPE_NONE)
            .setIsPlayable(true)
            .build()
    }

    @JvmStatic
    fun createTestMediaItem(mediaId : String) : MediaItem {
        return MediaItem
            .Builder()
            .setMediaId(mediaId)
            .setMediaMetadata(createTestMediaMetaData())
            .build()
    }

    @JvmStatic
    fun getDefaultLibraryParams() : MediaLibraryService.LibraryParams {
        return MediaLibraryService.LibraryParams
            .Builder()
            .build()
    }
}