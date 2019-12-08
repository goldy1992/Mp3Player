package com.github.goldy1992.mp3player.service.dagger.modules

import android.content.Context
import com.github.goldy1992.mp3player.service.library.search.FolderDao
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase
import com.github.goldy1992.mp3player.service.library.search.SongDao
import dagger.Module
import dagger.Provides
import org.mockito.Mockito
import javax.inject.Singleton

@Module
class MockSearchDatabaseModule {
    @Provides
    @Singleton
    fun providesSearchDb(context: Context?): SearchDatabase {
        return Mockito.mock(SearchDatabase::class.java)
    }

    @Provides
    @Singleton
    fun provideSongDao(searchDatabase: SearchDatabase?): SongDao {
        return Mockito.mock(SongDao::class.java)
    }

    @Provides
    @Singleton
    fun provideFolderDao(searchDatabase: SearchDatabase?): FolderDao {
        return Mockito.mock(FolderDao::class.java)
    }
}