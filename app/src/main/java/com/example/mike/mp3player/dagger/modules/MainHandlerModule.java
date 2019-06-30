package com.example.mike.mp3player.dagger.modules;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MainHandlerModule {

    @Provides
    @Named("main")
    public Handler provideMainHandler() {
        return new Handler(Looper.getMainLooper());
    }
}
