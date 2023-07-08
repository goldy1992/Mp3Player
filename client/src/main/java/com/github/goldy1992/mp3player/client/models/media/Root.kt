package com.github.goldy1992.mp3player.client.models.media

import com.github.goldy1992.mp3player.commons.Constants
import com.github.goldy1992.mp3player.commons.MediaItemType
import java.util.EnumMap

class Root(
    override val id: String = Constants.UNKNOWN,
    override val state: State = State.NOT_LOADED,
    val childMap : EnumMap<MediaItemType, MediaEntity> = EnumMap(MediaItemType::class.java)
) : MediaEntity {
    override val type: MediaItemType = MediaItemType.ROOT

    companion object {
        val NOT_LOADED = Root(state = State.NOT_LOADED)
        val LOADING = Root(state = State.LOADING)
        val NO_RESULTS = Root(state =  State.NO_RESULTS)
    }
}