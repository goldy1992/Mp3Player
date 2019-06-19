package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaRetrieverModule {

    private final MediaRetriever mediaRetriever;

    public MediaRetrieverModule(MediaRetriever mediaRetriever) {
        this.mediaRetriever = mediaRetriever;
    }

    @Provides
    @Singleton
    public MediaRetriever provideMediaRetriever() {
        return mediaRetriever;
    }
}
