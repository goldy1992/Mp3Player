package com.github.goldy1992.mp3player.client.utils

import android.os.Bundle
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import androidx.media3.session.MediaLibraryService.LibraryParams

object MediaLibraryParamUtils {

    fun getDefaultLibraryParams() : MediaLibraryService.LibraryParams {
        return MediaLibraryService.LibraryParams.Builder().build()
    }

    @UnstableApi
    fun getLibraryParams(extra: Bundle) : MediaLibraryService.LibraryParams {
        return LibraryParams.Builder().setExtras(extra).build()
    }
}