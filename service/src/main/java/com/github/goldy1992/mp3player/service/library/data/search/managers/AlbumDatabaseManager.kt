package com.github.goldy1992.mp3player.service.library.data.search.managers

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getAlbumTitle
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaId
import com.github.goldy1992.mp3player.commons.Normaliser.normalise
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.data.search.Album
import com.github.goldy1992.mp3player.service.library.data.search.SearchDatabase
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class AlbumDatabaseManager
    @Inject
    constructor(contentManager: ContentManager,
                mediaItemTypeIds: MediaItemTypeIds,
                searchDatabase: SearchDatabase
    )
    : SearchDatabaseManager<Album>(
        contentManager,
        searchDatabase.albumDao(),
        mediaItemTypeIds.getId(MediaItemType.ALBUMS)) {

    override fun createFromMediaItem(item: MediaItem): Album? {
        val id = getMediaId(item)
        val value = getAlbumTitle(item)
        return if (null != value) {
            Album(id, normalise(value))
        } else null
    }
}