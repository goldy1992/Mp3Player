package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.service.MediaPlaybackService;
import com.example.mike.mp3player.service.MyNotificationManager;
import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.session.MediaSessionAdapter;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @Singleton
    @Provides
    ServiceManager provideServiceManager(MediaSessionAdapter mediaSessionAdapter,
                                         MyNotificationManager myNotificationManager) {
        return new ServiceManager(mediaSessionAdapter, myNotificationManager);
    }

    @Singleton
    @Provides
    MyNotificationManager provideMyNotificationManager(Context context, MediaSessionCompat.Token token) {
        return new MyNotificationManager(context, token);
    }

}
