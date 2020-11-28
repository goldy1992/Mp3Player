package com.github.goldy1992.mp3player.service.library.content.filter

import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryPath
import java.io.File
import javax.inject.Inject

class SongsFromFolderResultsFilter @Inject constructor() : ResultsFilter {
    override fun filter(query: String,
                        results: MutableList<MediaBrowserCompat.MediaItem>?): List<MediaBrowserCompat.MediaItem>? {
        val queryPath = File(query)

        return results!!.filter {
            val directoryPath = getDirectoryPath(it)
            directoryPath != null && directoryPath.equals(queryPath.absolutePath.toUpperCase(), ignoreCase = true)
        }
    }
}