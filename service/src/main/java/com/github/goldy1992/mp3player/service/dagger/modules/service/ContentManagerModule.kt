package com.github.goldy1992.mp3player.service.dagger.modules.service

import com.github.goldy1992.mp3player.service.library.MediaContentManager
import com.github.goldy1992.mp3player.service.library.ContentManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
abstract class ContentManagerModule {

    @Binds
    @ServiceScoped
    abstract fun providesContentManager(mediaContentManager: MediaContentManager) : ContentManager

}