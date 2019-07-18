package com.example.mike.mp3player.dagger.modules;

import android.os.Handler;

import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.MyMetaDataCallback;
import com.example.mike.mp3player.client.callbacks.playback.MyPlaybackStateCallback;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaControllerCallbackModule {

    @Provides
    MyMediaControllerCallback provideMediaControllerCallback(MyMetaDataCallback myMetaDataCallback,
                                                             MyPlaybackStateCallback myPlaybackStateCallback) {
        return new MyMediaControllerCallback(myMetaDataCallback, myPlaybackStateCallback);
    }

    @Provides
    MyMetaDataCallback provideMetadataCallback(Handler handler) {
        return new MyMetaDataCallback(handler);
    }

    @Provides
    MyPlaybackStateCallback providePlaybackStateCallback(Handler handler) {
        return new MyPlaybackStateCallback(handler);
    }
}
