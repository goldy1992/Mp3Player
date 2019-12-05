package com.github.goldy1992.mp3player.service.library.search.managers;

import com.github.goldy1992.mp3player.commons.MediaItemType;

import java.util.EnumMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SearchDatabaseManagers {

    final EnumMap<MediaItemType, SearchDatabaseManager> dbManagerMap;

    /**
     * Constructor
     * @param songDatabaseManager SongDatabaseManager
     * @param folderDatabaseManager FolderDatabaseManager
     */
    @Inject
    public SearchDatabaseManagers(SongDatabaseManager songDatabaseManager,
                                  FolderDatabaseManager folderDatabaseManager) {
        this.dbManagerMap = new EnumMap<>(MediaItemType.class);
        this.dbManagerMap.put(MediaItemType.SONGS, songDatabaseManager);
        this.dbManagerMap.put(MediaItemType.FOLDERS, folderDatabaseManager);
    }

    /**
     * reindexes all of the search database tables.
     */
    public void reindexAll() {
        for (SearchDatabaseManager manager : dbManagerMap.values()) {
            manager.reindex();
        }
    }
}
