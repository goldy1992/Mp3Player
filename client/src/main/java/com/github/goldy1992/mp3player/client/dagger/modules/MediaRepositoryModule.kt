package com.github.goldy1992.mp3player.client.dagger.modules

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.github.goldy1992.mp3player.client.data.repositories.media.DefaultMediaRepository
import com.github.goldy1992.mp3player.client.data.repositories.media.MediaRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class MediaRepositoryModule {

    @OptIn(UnstableApi::class)
    @ActivityRetainedScoped
    @Binds
    abstract fun providesMediaRepo(
        defaultMediaBrowserRepository: DefaultMediaRepository
    ) : MediaRepository
}