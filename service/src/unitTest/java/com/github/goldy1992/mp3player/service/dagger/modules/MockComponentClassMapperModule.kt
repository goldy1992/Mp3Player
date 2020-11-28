package com.github.goldy1992.mp3player.service.dagger.modules

import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.service.MediaPlaybackService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
class MockComponentClassMapperModule {

    @Provides
    fun providesMockComponentClassMapper() : ComponentClassMapper {
        return ComponentClassMapper.Builder()
                .service(MediaPlaybackService::class.java)
                .build()
    }
}