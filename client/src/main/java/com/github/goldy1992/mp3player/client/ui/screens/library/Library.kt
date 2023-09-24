package com.github.goldy1992.mp3player.client.ui.screens.library

import com.github.goldy1992.mp3player.client.models.media.Albums
import com.github.goldy1992.mp3player.client.models.media.Folders
import com.github.goldy1992.mp3player.client.models.media.Playlist
import com.github.goldy1992.mp3player.client.models.media.Root
import com.github.goldy1992.mp3player.commons.MediaItemType
import java.util.EnumMap

/**
 * UI State Holder for all of the Library Objects.
 */
data class Library(
    val root : () -> Root = { Root.NOT_LOADED },
    val songs : () -> Playlist = { Playlist.NOT_LOADED },
    val albums : () -> Albums = { Albums.NOT_LOADED },
    val folders : () -> Folders = { Folders.NOT_LOADED },
    val onSelectedMap : () -> EnumMap<MediaItemType, Any> = { EnumMap(MediaItemType::class.java) }
) {
    companion object {
        val DEFAULT = Library()
    }
}