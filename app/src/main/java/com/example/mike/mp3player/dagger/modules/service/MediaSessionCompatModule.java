package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.service.player.DecreaseSpeedProvider;
import com.example.mike.mp3player.service.player.IncreaseSpeedProvider;
import com.example.mike.mp3player.service.player.MyMediaButtonEventHandler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaSessionCompatModule {

    private final String LOG_TAG = "MEDIA_SESSION_COMPAT";

    @Singleton
    @Provides
    public MediaSessionCompat provideMediaSessionCompat(Context context) {
        return new MediaSessionCompat(context, LOG_TAG);
    }

    @Singleton
    @Provides
    public MediaSessionCompat.Token provideMediaSessionToken(MediaSessionCompat mediaSessionCompat) {
        return mediaSessionCompat.getSessionToken();
    }

    @Provides
    @Singleton
    public IncreaseSpeedProvider providesIncreaseSpeedProvider() {
        return new IncreaseSpeedProvider();
    }

    @Provides
    @Singleton
    public DecreaseSpeedProvider providesDecreaseSpeedProvider() {
        return new DecreaseSpeedProvider();
    }

    @Provides
    @Singleton
    public MyMediaButtonEventHandler myMediaButtonEventHandler() {
        return new MyMediaButtonEventHandler();
    }
}
