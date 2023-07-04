package com.github.goldy1992.mp3player.client.models

import com.github.goldy1992.mp3player.commons.MediaItemType

data class RootChildren constructor(
    override val id: String = "",
    override val type: MediaItemType = MediaItemType.ROOT,
    val state: State = State.NOT_LOADED,
    val items: List<RootChild> = emptyList()
) : MediaEntity {

    companion object {
        val NOT_LOADED = RootChildren(state = State.NOT_LOADED)
        val LOADED = RootChildren(state = State.LOADED)
        val LOADING = RootChildren(state = State.LOADING)
        val NO_RESULTS = RootChildren(state = State.NO_RESULTS)
        val NO_PERMISSIONS = RootChildren(state = State.NO_PERMISSIONS)
    }
}