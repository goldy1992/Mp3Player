package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ExoPlayerModule {

    @Provides
    @Singleton
    public ExoPlayer provideExoPlayer(Context context) {
        return ExoPlayerFactory.newSimpleInstance(context);
    }
}
