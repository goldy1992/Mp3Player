package com.github.goldy1992.mp3player.service.dagger.modules

import android.content.Context
import com.github.goldy1992.mp3player.service.library.search.FolderDao
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase
import com.github.goldy1992.mp3player.service.library.search.SongDao
import dagger.Module
import dagger.Provides
import com.nhaarman.mockitokotlin2.*
import javax.inject.Singleton

@Module
class MockSearchDatabaseModule {

    val searchDatabase : SearchDatabase = mock<SearchDatabase>()
    val songDao : SongDao = mock<SongDao>()
    val folderDao : FolderDao = mock<FolderDao>()

    init
    {
        whenever(searchDatabase.folderDao()).thenReturn(folderDao)
        whenever(searchDatabase.songDao()).thenReturn(songDao)
    }

    @Provides
    @ComponentScope
    @Suppress("UNUSED_PARAMETER")
    fun providesSearchDb(context: Context?): SearchDatabase {
        return searchDatabase
    }

    @Provides
    @ComponentScope
    @Suppress("UNUSED_PARAMETER")
    fun provideSongDao(searchDatabase: SearchDatabase?): SongDao {
        return songDao
    }

    @Provides
    @ComponentScope
    @Suppress("UNUSED_PARAMETER")
    fun provideFolderDao(searchDatabase: SearchDatabase?): FolderDao {
        return folderDao
    }
}