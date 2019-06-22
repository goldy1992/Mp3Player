package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;
import android.os.Build;

import com.example.mike.mp3player.service.player.MarshmallowMediaPlayerAdapterBase;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;
import com.example.mike.mp3player.service.player.NougatMediaPlayerAdapterBase;
import com.example.mike.mp3player.service.player.OreoPlayerAdapterBase;
import com.example.mike.mp3player.service.session.MediaSessionCallback;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaPlayerAdapterModule {

    public MediaPlayerAdapterModule() {
    }

    @Singleton
    @Provides
    public MediaPlayerAdapterBase provideMediaPlayerAdapter(Context context, MediaSessionCallback mediaSessionCallback) {
        return createMediaPlayerAdapter(context, mediaSessionCallback);
    }


    /**
     * TODO: remove dependency of this method, initialise the object with Dagger2 and inject
     * @param context context
     * @return the appropriate Media Player object
     */
    private MediaPlayerAdapterBase createMediaPlayerAdapter(Context context, MediaSessionCallback mediaSessionCallback) {
        switch (Build.VERSION.SDK_INT) {
            case Build.VERSION_CODES.M:
                return new MarshmallowMediaPlayerAdapterBase(context, mediaSessionCallback, mediaSessionCallback);
            case Build.VERSION_CODES.N:
                return new NougatMediaPlayerAdapterBase(context, mediaSessionCallback, mediaSessionCallback);
            default: return new OreoPlayerAdapterBase(context, mediaSessionCallback, mediaSessionCallback);
        }
    }
}
