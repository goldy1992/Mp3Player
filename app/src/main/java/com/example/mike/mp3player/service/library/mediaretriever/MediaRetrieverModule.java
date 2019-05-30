package com.example.mike.mp3player.service.library.mediaretriever;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaRetrieverModule {

    private MediaRetriever mediaRetriever;

    public MediaRetrieverModule(MediaRetriever mediaRetriever) {
        this.mediaRetriever = mediaRetriever;
    }

    @Provides
    @Singleton
    MediaRetriever provideMediaRetriever() {
      return mediaRetriever;//  return new ContentResolverMediaRetriever()
    }
}
