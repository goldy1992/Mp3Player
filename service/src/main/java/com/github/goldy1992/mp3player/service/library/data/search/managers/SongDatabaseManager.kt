package com.github.goldy1992.mp3player.service.library.data.search.managers

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaId
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getTitle
import com.github.goldy1992.mp3player.commons.Normaliser.normalise
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.data.search.SearchDatabase
import com.github.goldy1992.mp3player.service.library.data.search.Song
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class SongDatabaseManager
    @Inject
    constructor(contentManager: ContentManager,
                rootItemId : String,
                searchDatabase: SearchDatabase
    )
    : SearchDatabaseManager<Song>(
        contentManager,
        searchDatabase.songDao(),
        rootItemId) {

    override fun createFromMediaItem(item: MediaItem): Song? {
        val id = getMediaId(item)
        val value = getTitle(item)
        return if (null != value) {
            Song(id, normalise(value))
        } else null
    }

    override fun getEntryId(entry: Song): String {
        return entry.id
    }

    override fun logTag(): String {
        return "SongDatabaseManager"
    }
}