package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.UserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.kotlin.mock
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
        return mock<UserPreferencesRepository>()
    }
}