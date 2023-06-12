package com.github.goldy1992.mp3player.service.library.content

import androidx.media3.common.MediaItem

data class ContentManagerResult(
    val children : List<MediaItem>,
    val numberOfResults : Int,
    val hasPermissions : Boolean
) {
    companion object {
        val DEFAULT = ContentManagerResult(emptyList(), 0, true)
    }
}