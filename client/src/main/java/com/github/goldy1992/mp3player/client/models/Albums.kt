package com.github.goldy1992.mp3player.client.models

import com.github.goldy1992.mp3player.commons.MediaItemType

class Albums
    constructor(
        override val id: String = MediaItemType.ALBUMS.name,
        val state: State = State.NOT_LOADED,
        val albums: List<Album> = emptyList()
    ) : MediaEntity {
    override val type: MediaItemType = MediaItemType.ALBUMS

    companion object {
        val NOT_LOADED = Albums(state = State.NOT_LOADED)
    }
    }