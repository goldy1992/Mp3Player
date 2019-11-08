package com.github.goldy1992.mp3player.dagger.modules.service;

import android.content.Context;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ExoPlayerModule {

    @Provides
    @Singleton
    public ExoPlayer provideExoPlayer(Context context) {
        SimpleExoPlayer simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build();
        simpleExoPlayer.setAudioAttributes(audioAttributes, true);
        return simpleExoPlayer;
    }
}
