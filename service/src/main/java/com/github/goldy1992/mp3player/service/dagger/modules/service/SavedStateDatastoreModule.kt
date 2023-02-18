package com.github.goldy1992.mp3player.service.dagger.modules.service

import com.github.goldy1992.mp3player.service.data.DefaultSavedStateRepository
import com.github.goldy1992.mp3player.service.data.ISavedStateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
abstract class SavedStateDatastoreModule {

    @ServiceScoped
    @Binds
    abstract fun providesISavedStateRepo(defaultSavedStateRepository: DefaultSavedStateRepository) : ISavedStateRepository
}