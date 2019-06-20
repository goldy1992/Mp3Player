package com.example.mike.mp3player.dagger.modules;

import android.os.Looper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LooperModule {

    private final Looper looper;

    public LooperModule(Looper looper) {
        this.looper = looper;
    }

    @Provides
    @Singleton
    public Looper provideLooper() {
        return looper;
    }
}
