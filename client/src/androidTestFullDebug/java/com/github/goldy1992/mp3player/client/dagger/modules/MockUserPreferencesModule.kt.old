package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.repositories.preferences.FakeUserPreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [UserPreferencesModule::class]
)
class MockUserPreferencesModule {

    @Provides
    fun provideMockPrefsRepo() : FakeUserPreferencesRepository {
        return FakeUserPreferencesRepository()
    }
}