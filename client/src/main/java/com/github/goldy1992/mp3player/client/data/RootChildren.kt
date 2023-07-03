package com.github.goldy1992.mp3player.client.data

import com.github.goldy1992.mp3player.client.ui.states.State

data class RootChildren constructor(
    val state: State = State.NOT_LOADED,
    val items: List<RootChild> = emptyList()
) {

    companion object {
        val NOT_LOADED = RootChildren(State.NOT_LOADED)
        val LOADED = RootChildren(State.LOADED)
        val LOADING = RootChildren(State.LOADING)
        val NO_RESULTS = RootChildren(State.NO_RESULTS)
        val NO_PERMISSIONS = RootChildren(State.NO_PERMISSIONS)
    }
}