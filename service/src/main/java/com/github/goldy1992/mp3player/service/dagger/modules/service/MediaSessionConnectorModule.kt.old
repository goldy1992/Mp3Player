package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import com.google.android.exoplayer2.upstream.ContentDataSource
import com.google.android.exoplayer2.upstream.FileDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@InstallIn(ServiceComponent::class)
@Module
class MediaSessionConnectorModule {
    @Provides
    @ServiceScoped
    fun providesContentDataSource(@ApplicationContext context: Context?): ContentDataSource {
        return ContentDataSource(context!!)
    }

    @Provides
    @ServiceScoped
    fun provideFileDataSource(): FileDataSource {
        return FileDataSource()
    }
}