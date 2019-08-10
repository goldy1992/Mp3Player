package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;

import com.example.mike.mp3player.service.library.mediaretriever.ContentResolverMediaRetriever;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaRetrieverModule {

    @Provides
    @Singleton
    public MediaRetriever provideMediaRetriever(Context context) {
        return selectMediaRetrieverToProvide(context);
    }

    private MediaRetriever selectMediaRetrieverToProvide(Context context) {
        return new ContentResolverMediaRetriever(context);
    }
}
