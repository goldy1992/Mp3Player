package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;
import android.os.Build;

import com.example.mike.mp3player.service.player.MarshmallowMediaPlayerAdapter;
import com.example.mike.mp3player.service.player.MediaPlayerAdapter;
import com.example.mike.mp3player.service.player.NougatMediaPlayerAdapter;
import com.example.mike.mp3player.service.player.OreoPlayerAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaPlayerAdapterModule {

    @Singleton
    @Provides
    public MediaPlayerAdapter provideMediaPlayerAdapter(Context context) {
        return createMediaPlayerAdapter(context);
    }

    /**
     * @return the appropriate Media Player object
     */
    private MediaPlayerAdapter createMediaPlayerAdapter(Context context) {
        switch (Build.VERSION.SDK_INT) {
            case Build.VERSION_CODES.M:
                return new MarshmallowMediaPlayerAdapter(context);
            case Build.VERSION_CODES.N:
                return new NougatMediaPlayerAdapter(context);
            default: return new OreoPlayerAdapter(context);
        }
    }
}
