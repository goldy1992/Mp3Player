package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import androidx.room.Room
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.library.search.FolderDao
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase.Companion.getDatabase
import com.github.goldy1992.mp3player.service.library.search.SongDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class SearchDatabaseModule {
    @Provides
    @ComponentScope
    fun providesSearchDb(context: Context): SearchDatabase {
        Room.databaseBuilder(context, SearchDatabase::class.java, DATABASE_NAME).build()
        return getDatabase(context)
    }

    @Provides
    @ComponentScope
    fun provideSongDao(searchDatabase: SearchDatabase): SongDao {
        return searchDatabase.songDao()
    }

    @Provides
    @ComponentScope
    fun provideFolderDao(searchDatabase: SearchDatabase): FolderDao {
        return searchDatabase.folderDao()
    }

    companion object {
        private const val DATABASE_NAME = "normalised-search-db"
    }
}