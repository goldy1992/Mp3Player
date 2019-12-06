package com.github.goldy1992.mp3player.service.dagger.modules.service;

import android.content.Context;

import androidx.room.Room;

import com.github.goldy1992.mp3player.service.library.search.FolderDao;
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase;
import com.github.goldy1992.mp3player.service.library.search.SongDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchDatabaseModule {

    private static final String DATABASE_NAME = "normalised-search-db";

    @Provides
    @Singleton
    public SearchDatabase providesSearchDb(Context context) {
        Room.databaseBuilder(context, SearchDatabase.class, DATABASE_NAME).build();
        return SearchDatabase.getDatabase(context);
    }

    @Provides
    @Singleton
    public SongDao provideSongDao(SearchDatabase searchDatabase) {
        return searchDatabase.songDao();
    }

    @Provides
    @Singleton
    public FolderDao provideFolderDao(SearchDatabase searchDatabase) {
        return searchDatabase.folderDao();
    }
}
