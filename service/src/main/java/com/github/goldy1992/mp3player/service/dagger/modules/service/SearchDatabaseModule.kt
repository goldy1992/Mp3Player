package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import androidx.room.Room
import com.github.goldy1992.mp3player.service.library.search.FolderDao
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase.Companion.getDatabase
import com.github.goldy1992.mp3player.service.library.search.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
class SearchDatabaseModule {
    @Provides
    @ServiceScoped
    fun providesSearchDb(@ApplicationContext context: Context): SearchDatabase {
        Room.databaseBuilder(context, SearchDatabase::class.java, DATABASE_NAME).build()
        return getDatabase(context)
    }

    @Provides
    @ServiceScoped
    fun provideSongDao(searchDatabase: SearchDatabase): SongDao {
        return searchDatabase.songDao()
    }

    @Provides
    @ServiceScoped
    fun provideFolderDao(searchDatabase: SearchDatabase): FolderDao {
        return searchDatabase.folderDao()
    }

    companion object {
        private const val DATABASE_NAME = "normalised-search-db"
    }
}