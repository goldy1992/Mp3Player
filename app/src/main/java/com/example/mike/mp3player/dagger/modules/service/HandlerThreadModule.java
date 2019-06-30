package com.example.mike.mp3player.dagger.modules.service;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class HandlerThreadModule {

    private static final String WORKER_ID = "MDIA_PLYBK_SRVC_WKR";

    @Provides
    public HandlerThread provideHandlerThread(String workerId) {
        HandlerThread handlerThread = new HandlerThread(WORKER_ID);
        handlerThread.start();
        handlerThread.getLooper().setMessageLogging((String x) -> {
            Log.i(WORKER_ID, x);
        });
        return handlerThread;
    }

    @Provides
    public Handler providesHandler(HandlerThread handlerThread) {
        return new Handler(handlerThread.getLooper());
    }

}
