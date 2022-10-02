package com.github.goldy1992.mp3player.service.library.content.filter

import androidx.media3.common.MediaItem

interface ResultsFilter {
    fun filter(query: String,
               results: MutableList<MediaItem>?):
            List<MediaItem>?
}