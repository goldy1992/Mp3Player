package com.github.goldy1992.mp3player.dagger.modules;

import android.content.Context;

import androidx.room.Room;

import com.github.goldy1992.mp3player.service.library.search.FolderDao;
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase;
import com.github.goldy1992.mp3player.service.library.search.SongDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class MockSearchDatabaseModule {

    @Provides
    @Singleton
    public SearchDatabase providesSearchDb(Context context) {
        return mock(SearchDatabase.class);
    }

    @Provides
    @Singleton
    public SongDao provideSongDao(SearchDatabase searchDatabase) {
        return mock(SongDao.class);
    }

    @Provides
    @Singleton
    public FolderDao provideFolderDao(SearchDatabase searchDatabase) {
        return mock(FolderDao.class);
    }
}

