package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.data.repositories.preferences.IUserPreferencesRepository
import com.github.goldy1992.mp3player.client.repositories.preferences.FakeUserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [IPreferencesRepositoryModule::class]
)
abstract class TestIPreferencesRepositoryModule {

    @Binds
    abstract fun testBindsIPreferencesRepo(fakeUserPreferencesRepository: FakeUserPreferencesRepository) : IUserPreferencesRepository
}