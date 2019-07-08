package com.example.mike.mp3player.dagger.modules.service;

import android.os.Handler;
import android.os.HandlerThread;

import com.example.mike.mp3player.dagger.components.fragments.PlaybackTrackerFragmentSubcomponent;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = PlaybackTrackerFragmentSubcomponent.class)
public class HandlerThreadModule {

    @Provides
    public HandlerThread provideHandlerThread(String workerId) {
        HandlerThread handlerThread = new HandlerThread(workerId);
        handlerThread.start();
//        handlerThread.getLooper().setMessageLogging((String x) -> {
//            Log.i(workerId, x);
//        });
        return handlerThread;
    }

    @Provides
    public Handler providesHandler(HandlerThread handlerThread) {
        return new Handler(handlerThread.getLooper());
    }

}
