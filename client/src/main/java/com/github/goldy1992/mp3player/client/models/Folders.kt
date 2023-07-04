package com.github.goldy1992.mp3player.client.models

import com.github.goldy1992.mp3player.commons.MediaItemType

data class Folders constructor(
    override val id: String = MediaItemType.FOLDERS.name,
    val state: State = State.NOT_LOADED,
    val folders: List<Folder> = emptyList()
) : MediaEntity {
    override val type: MediaItemType = MediaItemType.FOLDERS

    companion object {
        val NOT_LOADED = Folders(state = State.NOT_LOADED)
    }
}