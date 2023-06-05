package com.github.goldy1992.mp3player.service.library.content.filter

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryName
import org.apache.commons.lang3.StringUtils
import java.util.Locale
import javax.inject.Inject

class FolderSearchResultsFilter
    @Inject
    constructor() : ResultsFilter {

    private val locale = Locale.getDefault()

    override fun filter(query: String,
                        results: MutableList<MediaItem>?)
            : List<MediaItem> {

        return results!!.filter {
            val directoryName = getDirectoryName(it)
            StringUtils.contains(directoryName.uppercase(locale), query.uppercase(locale))
        }
    }
}