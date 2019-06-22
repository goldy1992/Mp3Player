package com.example.mike.mp3player.dagger.modules.service;

import android.content.Context;

import com.example.mike.mp3player.service.ServiceManager;
import com.example.mike.mp3player.service.player.MediaPlayerAdapterBase;
import com.example.mike.mp3player.service.session.AudioBecomingNoisyBroadcastReceiver;
import com.example.mike.mp3player.service.session.MediaSessionAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AudioBecomingNoisyBroadcastReceiverModule {

    @Provides
    @Singleton
    public AudioBecomingNoisyBroadcastReceiver providesAudioBecomingNoisyBroadcastReceiver(
            Context context, MediaPlayerAdapterBase mediaPlayerAdapterBase,
            MediaSessionAdapter mediaSessionAdapter, ServiceManager serviceManager) {
        return new AudioBecomingNoisyBroadcastReceiver(context, mediaSessionAdapter,
                mediaPlayerAdapterBase, serviceManager);
    }
}
