package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.data.sources.DefaultMediaDataSource
import com.github.goldy1992.mp3player.client.data.sources.MediaDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class PlaybackDataSourceModule {

    @ActivityRetainedScoped
    @Binds
    abstract fun providesPlaybackDataSource(
        defaultPlaybackDataSource: DefaultMediaDataSource
    ) : MediaDataSource
}