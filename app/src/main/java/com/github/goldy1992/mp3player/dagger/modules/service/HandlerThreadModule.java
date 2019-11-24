package com.github.goldy1992.mp3player.dagger.modules.service;

import android.os.Handler;
import android.os.HandlerThread;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class HandlerThreadModule {

    @Provides
    public HandlerThread provideHandlerThread(String workerId) {
        HandlerThread handlerThread = new HandlerThread(workerId);
        handlerThread.start();
        return handlerThread;
    }

    @Named("worker")
    @Provides
    public Handler providesHandler(HandlerThread handlerThread) {
        return new Handler(handlerThread.getLooper());
    }

}
