package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.repositories.media.MediaBrowserTestImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [MediaBrowserModule::class]
)
class MockMediaBrowserModule {

    @ActivityRetainedScoped
    @Provides
    fun providesMockMediaBrowser() : MediaBrowserTestImpl {
        return MediaBrowserTestImpl()
    }
}