package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.media.IMediaBrowser
import com.github.goldy1992.mp3player.client.media.IMediaBrowserTestImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.testing.TestInstallIn


@TestInstallIn(
    components = [ActivityRetainedComponent::class],
    replaces = [IMediaBrowserModule::class])
@Module
abstract class MockIMediaBrowserModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun providesIMediaBrowser(mediaBrowser: IMediaBrowserTestImpl) : IMediaBrowser
}