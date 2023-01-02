package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class IPreferencesRepositoryModule {

    @Binds
    abstract fun bindsIPreferencesRepo(userPreferencesRepository: UserPreferencesRepository) :
        IUserPreferencesRepository
}