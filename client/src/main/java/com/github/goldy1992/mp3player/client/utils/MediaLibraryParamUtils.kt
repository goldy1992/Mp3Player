package com.github.goldy1992.mp3player.client.utils

import androidx.media3.session.MediaLibraryService

object MediaLibraryParamUtils {

    fun getDefaultLibraryParams() : MediaLibraryService.LibraryParams {
        return MediaLibraryService.LibraryParams.Builder().build()
    }
}