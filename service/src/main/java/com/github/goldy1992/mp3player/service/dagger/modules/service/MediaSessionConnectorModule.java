package com.github.goldy1992.mp3player.service.dagger.modules.service;

import android.content.Context;

import com.google.android.exoplayer2.upstream.ContentDataSource;
import com.google.android.exoplayer2.upstream.FileDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaSessionConnectorModule {

    @Provides
    @Singleton
    public ContentDataSource providesContentDataSource(Context context) {
        return new ContentDataSource(context);
    }

    @Provides
    @Singleton
    public FileDataSource provideFileDataSource() {
        return new FileDataSource();
    }

}
