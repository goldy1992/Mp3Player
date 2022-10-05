package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@InstallIn(ServiceComponent::class)
@Module
class MediaSourceFactoryModule {

    @Provides
    fun providesMediaSourceFactory(@ApplicationContext context: Context) : MediaSource.Factory {
        return DefaultMediaSourceFactory(context)
    }
}