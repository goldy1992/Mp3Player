package com.example.mike.mp3player.dagger.modules.service;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class HandlerThreadModule {

    private static final String WORKER_ID = "MDIA_PLYBK_SRVC_WKR";
    private final HandlerThread handlerThread;

    public HandlerThreadModule() {
        HandlerThread newHandlerThread = new HandlerThread(WORKER_ID);
        newHandlerThread.start();
        newHandlerThread.getLooper().setMessageLogging((String x) -> {
            Log.i(WORKER_ID, x);
        });
        this.handlerThread = newHandlerThread;
    }

    @Singleton
    @Provides
    public HandlerThread provideHandlerThread() {
        return handlerThread;
    }

    @Provides
    public Handler providesHandler() {
        return new Handler(handlerThread.getLooper());
    }
}
