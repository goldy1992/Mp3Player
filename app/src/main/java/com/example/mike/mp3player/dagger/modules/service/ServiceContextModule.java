package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceContextModule {

    private final Context context;

    public ServiceContextModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    Context provideServiceContext() {
        return context;
    }
}
