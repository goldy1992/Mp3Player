package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import com.google.android.exoplayer2.upstream.ContentDataSource
import com.google.android.exoplayer2.upstream.FileDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MediaSessionConnectorModule {
    @Provides
    @Singleton
    fun providesContentDataSource(context: Context?): ContentDataSource {
        return ContentDataSource(context)
    }

    @Provides
    @Singleton
    fun provideFileDataSource(): FileDataSource {
        return FileDataSource()
    }
}