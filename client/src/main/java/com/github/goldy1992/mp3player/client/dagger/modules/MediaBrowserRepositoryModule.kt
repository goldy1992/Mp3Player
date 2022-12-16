package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.data.audiobands.media.browser.DefaultMediaBrowserRepository
import com.github.goldy1992.mp3player.client.data.audiobands.media.browser.MediaBrowserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class MediaBrowserRepositoryModule {

    @Binds
    abstract fun providesMediaBrowserRepo(
        defaultMediaBrowserRepository: DefaultMediaBrowserRepository
    ) : MediaBrowserRepository
}