package com.github.goldy1992.mp3player.service.library.data.search.managers

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getAlbumTitle
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaId
import com.github.goldy1992.mp3player.commons.Normaliser.normalise
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.data.search.Album
import com.github.goldy1992.mp3player.service.library.data.search.SearchDatabase

class AlbumDatabaseManager

    constructor(contentManager: ContentManager,
                rootItemId : String,
                searchDatabase: SearchDatabase
    )
    : SearchDatabaseManager<Album>(
        contentManager,
        searchDatabase.albumDao(),
        rootItemId) {

    override fun createFromMediaItem(item: MediaItem): Album {
        val id = getMediaId(item)
        val value = getAlbumTitle(item)
        return Album(id, normalise(value))
    }

    override fun getEntryId(entry: Album): String {
        return entry.id
    }

    override fun logTag(): String {
        return "AlbumDatabaseManager"
    }
}