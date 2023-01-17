package com.github.goldy1992.mp3player.client.data

import com.github.goldy1992.mp3player.client.ui.states.State

data class SearchResults(
    val state : State = State.NOT_LOADED,
    val songs: Songs = Songs(),
    val folders: Folders = Folders(),
    val albums: Albums = Albums()
) {
    companion object {
        val NO_RESULTS = SearchResults(State.NO_RESULTS)
    }
}