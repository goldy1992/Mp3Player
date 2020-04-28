package com.github.goldy1992.mp3player.service.dagger.modules.service

import android.content.Context
import com.github.goldy1992.mp3player.commons.dagger.scopes.ComponentScope
import com.google.android.exoplayer2.upstream.ContentDataSource
import com.google.android.exoplayer2.upstream.FileDataSource
import dagger.Module
import dagger.Provides

@Module
class MediaSessionConnectorModule {
    @Provides
    @ComponentScope
    fun providesContentDataSource(context: Context?): ContentDataSource {
        return ContentDataSource(context)
    }

    @Provides
    @ComponentScope
    fun provideFileDataSource(): FileDataSource {
        return FileDataSource()
    }
}