package com.github.goldy1992.mp3player.dagger.modules

import com.github.goldy1992.mp3player.client.activities.*
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import com.github.goldy1992.mp3player.service.MediaPlaybackService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
class ComponentClassMapperModule {

    @Singleton
    @Provides
    fun providesComponentClassMapper() : ComponentClassMapper {
        return ComponentClassMapper.Builder()
                .splashActivity(SplashScreenEntryActivity::class.java)
                .mainActivity(MainActivity::class.java)
                .service(MediaPlaybackService::class.java)
                .build()
    }
}