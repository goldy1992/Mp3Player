package com.github.goldy1992.mp3player.service.library.content.filter

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryName
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

class FolderSearchResultsFilter
    @Inject
    constructor() : ResultsFilter {

    override fun filter(query: String,
                        results: MutableList<MediaItem>?)
            : List<MediaItem>? {

        return results!!.filter {
            val directoryName = getDirectoryName(it)
            null != directoryName && StringUtils.contains(directoryName.toUpperCase(), query.toUpperCase())
        }
    }
}