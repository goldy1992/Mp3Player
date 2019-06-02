package com.example.mike.mp3player.service.library;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaLibraryModule {
    private final Context context;

    public MediaLibraryModule(Context context) {
        this.context = context;
    }



}
