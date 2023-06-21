package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.media.DefaultMediaBrowser2
import com.github.goldy1992.mp3player.client.media.IMediaBrowser
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class IMediaBrowserModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun providesIMediaBrowser(defaultMediaBrowser: DefaultMediaBrowser2) : IMediaBrowser
}