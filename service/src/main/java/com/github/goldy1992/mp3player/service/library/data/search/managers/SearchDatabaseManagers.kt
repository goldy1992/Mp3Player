package com.github.goldy1992.mp3player.service.library.data.search.managers

import android.util.Log
import com.github.goldy1992.mp3player.commons.IoDispatcher
import com.github.goldy1992.mp3player.commons.LogTagger
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaItemTypeIds
import com.github.goldy1992.mp3player.service.library.data.search.SearchDatabase
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@ServiceScoped
class SearchDatabaseManagers
/**
 * Constructor
 * @param songDatabaseManager SongDatabaseManager
 * @param folderDatabaseManager FolderDatabaseManager
 */
    @Inject
    constructor(
        mediaItemTypeIds: MediaItemTypeIds,
        contentManager : ContentManager,
        searchDatabase: SearchDatabase,
        scope: CoroutineScope,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ) : LogTagger {

    private val songDatabaseManager: SongDatabaseManager
    private val folderDatabaseManager: FolderDatabaseManager
    private val albumDatabaseManager: AlbumDatabaseManager

    private val dbManagerMap: EnumMap<MediaItemType, SearchDatabaseManager<*>> = EnumMap(MediaItemType::class.java)

    init {

        songDatabaseManager = SongDatabaseManager(
            contentManager,
            mediaItemTypeIds.getId(MediaItemType.SONGS),
            searchDatabase
        )

        folderDatabaseManager = FolderDatabaseManager(
            contentManager,
            mediaItemTypeIds.getId(MediaItemType.FOLDERS),
            searchDatabase
        )

        albumDatabaseManager = AlbumDatabaseManager(
            contentManager,
            mediaItemTypeIds.getId(MediaItemType.ALBUMS),
            searchDatabase
        )
        scope.launch(ioDispatcher) {
            Log.v(logTag(), "init() launching contentManager.isInitialised.collect on ioDispatcher")
            contentManager.isInitialised.collect { isInitialised ->
                if (isInitialised) {
                    reindexAll()
                }
            }
        }
    }
    /**
     * re-indexes all of the search database tables.
     */
    suspend fun reindexAll() {
        for (manager in dbManagerMap.values) {
            manager.reindex()
        }
    }


    init {
        dbManagerMap[MediaItemType.SONGS] = songDatabaseManager
        dbManagerMap[MediaItemType.FOLDERS] = folderDatabaseManager
        dbManagerMap[MediaItemType.ALBUMS] = albumDatabaseManager
    }

    override fun logTag(): String {
        return "SearchDatabaseManagers"
    }
}