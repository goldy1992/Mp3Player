package com.example.mike.mp3player.dagger.modules;

import android.os.Handler;

import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.metadata.MyMetadataCallback;
import com.example.mike.mp3player.client.callbacks.playback.MyPlaybackStateCallback;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaControllerCallbackModule {

    @Provides
    MyMediaControllerCallback provideMediaControllerCallback(MyMetadataCallback myMetaDataCallback,
                                                             MyPlaybackStateCallback myPlaybackStateCallback) {
        return new MyMediaControllerCallback(myMetaDataCallback, myPlaybackStateCallback);
    }

    @Provides
    MyMetadataCallback provideMetadataCallback(@Named("main") Handler handler) {
        return new MyMetadataCallback(handler);
    }

    @Provides
    MyPlaybackStateCallback providePlaybackStateCallback(@Named("main") Handler handler) {
        return new MyPlaybackStateCallback(handler);
    }
}
