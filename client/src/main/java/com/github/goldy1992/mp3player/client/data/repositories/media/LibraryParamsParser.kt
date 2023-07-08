package com.github.goldy1992.mp3player.client.data.repositories.media

import android.os.Bundle
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaLibraryService
import com.github.goldy1992.mp3player.commons.Constants.HAS_PERMISSIONS

@UnstableApi
object LibraryParamsParser {

    fun parse(mediaLibraryParams: MediaLibraryService.LibraryParams?) : Bundle {
        val toReturn = Bundle()
        if (mediaLibraryParams == null) {
            return toReturn
        }

        val hasPermissions = mediaLibraryParams.extras.getBoolean(HAS_PERMISSIONS,false)
        toReturn.putBoolean(HAS_PERMISSIONS, hasPermissions)

        return toReturn
    }
}