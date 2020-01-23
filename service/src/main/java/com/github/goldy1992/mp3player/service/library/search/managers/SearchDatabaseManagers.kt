package com.github.goldy1992.mp3player.service.library.search.managers

import com.github.goldy1992.mp3player.commons.MediaItemType
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchDatabaseManagers @Inject constructor(songDatabaseManager: SongDatabaseManager,
                                                 folderDatabaseManager: FolderDatabaseManager) {
    val dbManagerMap: EnumMap<MediaItemType, SearchDatabaseManager<*>>
    /**
     * reindexes all of the search database tables.
     */
    suspend fun reindexAll() {
        for (manager in dbManagerMap.values) {
            manager.reindex()
        }
    }

    /**
     * Constructor
     * @param songDatabaseManager SongDatabaseManager
     * @param folderDatabaseManager FolderDatabaseManager
     */
    init {
        dbManagerMap = EnumMap(MediaItemType::class.java)
        dbManagerMap[MediaItemType.SONGS] = songDatabaseManager
        dbManagerMap[MediaItemType.FOLDERS] = folderDatabaseManager
    }
}