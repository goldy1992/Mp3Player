package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.service.PlaybackManager;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;
import com.example.mike.mp3player.service.session.MediaSessionAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaSessionCompatModule {

    private final String LOG_TAG;


    public MediaSessionCompatModule(final String logTag) {
        this.LOG_TAG = logTag;
    }

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

    @Singleton
    @Provides
    public MediaSessionAdapter mediaSessionAdapter(MediaSessionCompat mediaSession,
                                   PlaybackManager playbackManager, MediaPlayerAdapterBase mediaPlayerAdapterBase) {
        return new MediaSessionAdapter(mediaSession, playbackManager, mediaPlayerAdapterBase);
    }

}
