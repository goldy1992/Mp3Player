package com.github.goldy1992.mp3player.client.dagger.modules

import android.app.Service
import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class ComponentClassMapperModule {

    @Singleton
    @Provides
    fun providesComponentClassMapper() : ComponentClassMapper {
        return ComponentClassMapper.Builder()
                .service(Service::class.java)
                .build()
    }
}