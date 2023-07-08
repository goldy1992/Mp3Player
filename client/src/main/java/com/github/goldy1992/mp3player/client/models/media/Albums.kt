package com.github.goldy1992.mp3player.client.models.media

import com.github.goldy1992.mp3player.commons.MediaItemType

class Albums
    constructor(
        override val id: String = MediaItemType.ALBUMS.name,
        val albums: List<Album> = emptyList(),
        override val state: State = State.NOT_LOADED
    ) : MediaEntity {
    override val type: MediaItemType = MediaItemType.ALBUMS

    companion object {
        val NOT_LOADED = Albums()
    }
    }