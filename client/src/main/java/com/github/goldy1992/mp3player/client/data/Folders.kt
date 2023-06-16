package com.github.goldy1992.mp3player.client.data

import com.github.goldy1992.mp3player.client.ui.states.State

data class Folders constructor(
    val state: State = State.NOT_LOADED,
    val folders: List<Folder> = emptyList()
) : MediaEntity