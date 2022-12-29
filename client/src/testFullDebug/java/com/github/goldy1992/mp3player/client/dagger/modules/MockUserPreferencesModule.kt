package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.MockUserPreferencesRepository
import com.github.goldy1992.mp3player.client.data.repositories.preferences.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [UserPreferencesModule::class]
)
class MockUserPreferencesModule {

    @Provides
    @Singleton
    fun provideMockPrefsRepo() : UserPreferencesRepository {
        return MockUserPreferencesRepository()
    }
}