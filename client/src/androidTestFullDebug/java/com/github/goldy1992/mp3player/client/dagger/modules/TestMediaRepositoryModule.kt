package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import com.github.goldy1992.mp3player.client.repositories.media.TestMediaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [MediaRepositoryModule::class]
)
abstract class TestMediaRepositoryModule {

    @Binds
    abstract fun providesTestMediaRepo(testMediaRepository: TestMediaRepository) : MediaRepository
}