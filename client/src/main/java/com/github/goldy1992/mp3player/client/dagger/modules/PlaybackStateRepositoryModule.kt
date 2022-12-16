package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.client.data.audiobands.media.controller.DefaultPlaybackStateRepository
import com.github.goldy1992.mp3player.client.data.audiobands.media.controller.PlaybackStateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class PlaybackStateRepositoryModule {

    @Binds
    abstract fun buildPlaybackStateRepo(defaultPlaybackStateRepository: DefaultPlaybackStateRepository)
    : PlaybackStateRepository
}