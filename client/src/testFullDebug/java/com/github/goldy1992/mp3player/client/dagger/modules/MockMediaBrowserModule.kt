package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.media.IMediaBrowserTestImpl
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
    fun providesMockMediaBrowser() : IMediaBrowserTestImpl {
        return IMediaBrowserTestImpl()
    }
}