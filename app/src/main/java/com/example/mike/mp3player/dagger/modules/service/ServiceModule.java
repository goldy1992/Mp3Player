package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.MyNotificationManager;
import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.session.MediaSessionAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {
    private final MediaPlaybackService service;

    public ServiceModule(MediaPlaybackService service) {
        this.service = service;
    }

    @Singleton
    @Provides
    ServiceManager provideServiceManager(MediaSessionAdapter mediaSessionAdapter,
                                         MyNotificationManager myNotificationManager) {
        return new ServiceManager(service, mediaSessionAdapter, myNotificationManager);
    }

    @Singleton
    @Provides
    MyNotificationManager provideMyNotificationManager() {
        return new MyNotificationManager(service);
    }

    @Singleton
    @Provides
    MediaPlaybackService provideMediaPlaybackService() {
        return this.service;
    }

}
