package com.github.goldy1992.mp3player.service.library.search.managers

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaId
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getTitle
import com.github.goldy1992.mp3player.commons.Normaliser.normalise
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase
import com.github.goldy1992.mp3player.service.library.search.Song
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class SongDatabaseManager
    @Inject
    constructor(contentManager: ContentManager,
                mediaItemTypeIds: MediaItemTypeIds,
                searchDatabase: SearchDatabase)
    : SearchDatabaseManager<Song>(
        contentManager,
        searchDatabase.songDao(),
        mediaItemTypeIds.getId(MediaItemType.SONGS)) {

    override fun createFromMediaItem(item: MediaItem): Song? {
        val id = getMediaId(item)
        val value = getTitle(item)
        return if (null != value) {
            Song(id, normalise(value))
        } else null
    }
}