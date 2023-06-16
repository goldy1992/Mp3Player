package com.github.goldy1992.mp3player.client.data

import com.github.goldy1992.mp3player.client.ui.states.State

data class RootItems constructor(
    val state: State = State.NOT_LOADED,
    val items: List<RootItem> = emptyList()
) {

    companion object {
        val NOT_LOADED = RootItems(State.NOT_LOADED)
        val LOADED = RootItems(State.LOADED)
        val LOADING = RootItems(State.LOADING)
        val NO_RESULTS = RootItems(State.NO_RESULTS)
        val NO_PERMISSIONS = RootItems(State.NO_PERMISSIONS)
    }
}