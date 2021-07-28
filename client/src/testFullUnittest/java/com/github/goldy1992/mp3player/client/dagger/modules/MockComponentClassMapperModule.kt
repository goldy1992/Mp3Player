package com.github.goldy1992.mp3player.client.dagger.modules

import com.github.goldy1992.mp3player.commons.ComponentClassMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class MockComponentClassMapperModule {

    @Provides
    fun providesMockComponentClassMapper() : ComponentClassMapper {
        return ComponentClassMapper.Builder()
                .service(ServiceComponent::class.java)
                .mainActivity(ActivityComponent::class.java)
                .build()
    }
}