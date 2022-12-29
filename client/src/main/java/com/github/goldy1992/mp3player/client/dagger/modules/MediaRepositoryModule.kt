package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.data.repositories.media.DefaultMediaRepository
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class MediaRepositoryModule {

    @Binds
    abstract fun providesMediaRepo(
        defaultMediaBrowserRepository: DefaultMediaRepository
    ) : MediaRepository
}