package com.example.mike.mp3player.service.library;

import android.content.Context;

import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaLibraryModule {
    private final Context context;

    public MediaLibraryModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    public MediaLibrary provideMediaLibrary(Context context, MediaRetriever mediaRetriever) {
        return new MediaLibrary(context, mediaRetriever);
    }


    @Provides
    public Context provideContext() {
        return context;
    }



}
