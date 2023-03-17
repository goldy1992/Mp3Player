package com.github.goldy1992.mp3player.service.library.data.search.managers

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryName
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryPath
import com.github.goldy1992.mp3player.commons.Normaliser.normalise
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.data.search.Folder
import com.github.goldy1992.mp3player.service.library.data.search.SearchDatabase
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Inject

@ServiceScoped
class FolderDatabaseManager
    @Inject
    constructor(contentManager: ContentManager,
                mediaItemTypeIds: MediaItemTypeIds,
                searchDatabase: SearchDatabase
    )
    : SearchDatabaseManager<Folder>(
        contentManager,
        searchDatabase.folderDao(),
        mediaItemTypeIds.getId(MediaItemType.FOLDERS)) {

    public override fun createFromMediaItem(item: MediaItem): Folder? {
        val id = getDirectoryPath(item)
        val value = getDirectoryName(item)
        return if (null != value) {
            Folder(id, normalise(value))
        } else null
    }
}