package com.github.goldy1992.mp3player.service.dagger.modules.service

import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import com.github.goldy1992.mp3player.service.library.ContentManager
import com.github.goldy1992.mp3player.service.library.MediaContentManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
abstract class ContentManagerModule {

    @OptIn(UnstableApi::class)
    @Binds
    @ServiceScoped
    abstract fun providesContentManager(mediaContentManager: MediaContentManager) : ContentManager

}