package com.github.goldy1992.mp3player.service.library.content.filter

import android.support.v4.media.MediaBrowserCompat.MediaItem

interface ResultsFilter {
    fun filter(query: String,
               results: List<MediaItem>?):
            List<MediaItem>?
}