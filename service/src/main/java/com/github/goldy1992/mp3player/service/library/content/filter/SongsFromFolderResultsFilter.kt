package com.github.goldy1992.mp3player.service.library.content.filter

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryPath
import java.io.File
import java.util.Locale
import javax.inject.Inject

class SongsFromFolderResultsFilter @Inject constructor() : ResultsFilter {
    override fun filter(query: String,
                        results: MutableList<MediaItem>?): List<MediaItem> {
        val queryPath = File(query)

        return results!!.filter {
            val directoryPath = getDirectoryPath(it)
            directoryPath != null && directoryPath.equals(queryPath.absolutePath.uppercase(Locale.ROOT), ignoreCase = true)
        }
    }
}