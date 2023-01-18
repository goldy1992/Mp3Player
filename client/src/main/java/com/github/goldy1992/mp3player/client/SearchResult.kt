package com.github.goldy1992.mp3player.client

import com.github.goldy1992.mp3player.commons.MediaItemType

data class SearchResult(
    val mediaItemType: MediaItemType = MediaItemType.ROOT,
    val value : Any = Any()
) {
    companion object {
        val EMPTY = SearchResult()
    }
}