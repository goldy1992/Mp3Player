package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;

import com.example.mike.mp3player.service.session.AudioBecomingNoisyBroadcastReceiver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AudioBecomingNoisyBroadcastReceiverModule {

    @Provides
    @Singleton
    public AudioBecomingNoisyBroadcastReceiver providesAudioBecomingNoisyBroadcastReceiver(
            Context context) {
        return new AudioBecomingNoisyBroadcastReceiver(context);
    }
}
