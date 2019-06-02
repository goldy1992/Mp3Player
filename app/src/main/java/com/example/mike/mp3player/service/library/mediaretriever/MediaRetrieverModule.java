package com.example.mike.mp3player.service.library.mediaretriever;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

public abstract class MediaRetrieverModule {

    protected final Context context;

    public MediaRetrieverModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public abstract MediaRetriever provideMediaRetriever();
}
