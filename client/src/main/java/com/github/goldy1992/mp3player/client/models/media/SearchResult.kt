package com.github.goldy1992.mp3player.client.models.media

import com.github.goldy1992.mp3player.commons.MediaItemType

data class SearchResult(
    override val id: String = "",
    override val type: MediaItemType = MediaItemType.ROOT,
    val value : MediaEntity = Song.DEFAULT,
    override val state: State = State.NOT_LOADED,
) : MediaEntity {
    companion object {
        val EMPTY = SearchResult()
    }
}