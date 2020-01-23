package com.github.goldy1992.mp3player.service.library.search.managers

import android.os.Handler
import android.support.v4.media.MediaBrowserCompat
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryName
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryPath
import com.github.goldy1992.mp3player.commons.Normaliser.normalise
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.search.Folder
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class FolderDatabaseManager
    @Inject
    constructor(contentManager: ContentManager,
                @Named("worker") handler: Handler,
                mediaItemTypeIds: MediaItemTypeIds,
                searchDatabase: SearchDatabase)
    : SearchDatabaseManager<Folder>(
        contentManager,
        handler,
        searchDatabase.folderDao(),
        mediaItemTypeIds.getId(MediaItemType.FOLDERS)) {

    public override fun createFromMediaItem(item: MediaBrowserCompat.MediaItem): Folder? {
        val id = getDirectoryPath(item)
        val value = getDirectoryName(item)
        return if (null != value) {
            Folder(id, normalise(value))
        } else null
    }
}