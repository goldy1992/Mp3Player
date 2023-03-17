package com.github.goldy1992.mp3player.client.data

import com.github.goldy1992.mp3player.client.ui.states.State

class Albums
    constructor(
        val state: State = State.NOT_LOADED,
        val albums: List<Album> = emptyList()
    ) : MediaEntity