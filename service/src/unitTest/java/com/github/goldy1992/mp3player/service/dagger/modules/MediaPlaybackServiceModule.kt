package com.github.goldy1992.mp3player.service.dagger.modules

import com.github.goldy1992.mp3player.service.MediaPlaybackService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent

@InstallIn(ServiceComponent::class)
@Module
abstract class MediaPlaybackServiceModule {

@Binds
abstract fun mediaPlaybackService(mediaPlaybackService: MediaPlaybackService) : MediaPlaybackService

}