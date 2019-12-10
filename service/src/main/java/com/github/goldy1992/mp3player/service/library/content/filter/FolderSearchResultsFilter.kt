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
        val iterator: MutableListIterator<MediaItem> = results!!.toMutableList().listIterator()
        while (iterator.hasNext()) {
            val currentItem = iterator.next()
            val directoryName = getDirectoryName(currentItem!!)
            if (null != directoryName && !StringUtils.contains(directoryName.toUpperCase(), query.toUpperCase())) {
                iterator.remove()
            }
        }
        return results
    }
}