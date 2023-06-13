package com.github.goldy1992.mp3player.service.library.data.search.managers

import androidx.media3.common.MediaItem
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryName
import com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryPath
import com.github.goldy1992.mp3player.commons.Normaliser.normalise
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.data.search.Folder
import com.github.goldy1992.mp3player.service.library.data.search.SearchDatabase


class FolderDatabaseManager

    constructor(contentManager: ContentManager,
                rootItemId : String,
                searchDatabase: SearchDatabase
    )
    : SearchDatabaseManager<Folder>(
        contentManager,
        searchDatabase.folderDao(),
    rootItemId) {

    override fun createFromMediaItem(item: MediaItem): Folder? {
        val id = getDirectoryPath(item)
        val value = getDirectoryName(item)
        return if (null != value) {
            Folder(id, normalise(value))
        } else null
    }

    override fun getEntryId(entry: Folder): String {
        return entry.id
    }

    override fun logTag(): String {
        return "FolderDatabaseManager"
    }
}