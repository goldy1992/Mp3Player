package com.github.goldy1992.mp3player.service.dagger.modules

import android.content.Context
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.github.goldy1992.mp3player.service.library.search.FolderDao
import com.github.goldy1992.mp3player.service.library.search.SearchDatabase
import com.github.goldy1992.mp3player.service.library.search.SongDao
import dagger.Module
import dagger.Provides
import com.nhaarman.mockitokotlin2.*
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Singleton

@InstallIn(ServiceComponent::class)
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
    @ServiceScoped
    @Suppress("UNUSED_PARAMETER")
    fun providesSearchDb(@ApplicationContext context: Context): SearchDatabase {
        return searchDatabase
    }

    @Provides
    @ServiceScoped
    @Suppress("UNUSED_PARAMETER")
    fun provideSongDao(searchDatabase: SearchDatabase?): SongDao {
        return songDao
    }

    @Provides
    @ServiceScoped
    @Suppress("UNUSED_PARAMETER")
    fun provideFolderDao(searchDatabase: SearchDatabase?): FolderDao {
        return folderDao
    }
}