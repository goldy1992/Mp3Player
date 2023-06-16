package com.github.goldy1992.mp3player.service.dagger.modules.service

import com.github.goldy1992.mp3player.service.library.data.search.managers.SearchDatabaseManagers
import com.github.goldy1992.mp3player.service.library.data.search.managers.SearchDatabaseManagersFullImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

@Module
@InstallIn(ServiceComponent::class)
abstract class SearchDatabaseManagersModule {

    @Binds
    abstract fun bindsSearchDatabaseManagers(searchDatabaseManagersFullImpl: SearchDatabaseManagersFullImpl) :
            SearchDatabaseManagers
}