package com.example.mike.mp3player.dagger.modules.service;

import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaPlayerAdapterModule {

    private final MediaPlayerAdapterBase mediaPlayerAdapterBase;

    public MediaPlayerAdapterModule(MediaPlayerAdapterBase mediaPlayerAdapterBase) {
        this.mediaPlayerAdapterBase = mediaPlayerAdapterBase;
    }

    @Singleton
    @Provides
    public MediaPlayerAdapterBase provideMediaPlayerAdapter() {
        return mediaPlayerAdapterBase;
    }
}
