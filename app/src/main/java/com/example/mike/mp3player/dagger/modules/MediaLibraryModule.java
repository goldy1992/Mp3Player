package com.example.mike.mp3player.dagger.modules;

import android.content.Context;

import com.example.mike.mp3player.service.library.MediaLibrary;
import com.example.mike.mp3player.service.library.mediaretriever.MediaRetriever;
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
    public MediaLibrary provideMediaLibrary(MediaRetriever mediaRetriever) {
        return new MediaLibrary(mediaRetriever);
    }


    @Provides
    public Context provideContext() {
        return context;
    }



}
