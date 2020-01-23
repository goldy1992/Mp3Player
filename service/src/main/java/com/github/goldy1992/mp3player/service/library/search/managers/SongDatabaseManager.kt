package com.github.goldy1992.mp3player.service.library.search.managers

import android.os.Handler
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaId
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getTitle
import com.github.goldy1992.mp3player.commons.Normaliser.normalise
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase
import com.github.goldy1992.mp3player.service.library.search.Song
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SongDatabaseManager
    @Inject
    constructor(contentManager: ContentManager,
                @Named("worker") handler: Handler,
                mediaItemTypeIds: MediaItemTypeIds,
                searchDatabase: SearchDatabase)
    : SearchDatabaseManager<Song>(
        contentManager,
        handler,
        searchDatabase.songDao(),
        mediaItemTypeIds.getId(MediaItemType.SONGS)) {

    public override fun createFromMediaItem(item: MediaBrowserCompat.MediaItem): Song? {
        val id = getMediaId(item)
        val value = getTitle(item)
        return if (null != value) {
            Song(id, normalise(value))
        } else null
    }
}