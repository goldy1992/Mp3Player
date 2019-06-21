package com.example.mike.mp3player.dagger.modules;

import android.content.Context;
import android.os.Looper;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.MyMetaDataCallback;
import com.example.mike.mp3player.client.callbacks.playback.MyPlaybackStateCallback;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaControllerAdapterModule {

    @Provides
    @Singleton
    MediaControllerAdapter provideMediaControllerAdapter(Context context, MyMediaControllerCallback myMediaControllerCallback) {
        return new MediaControllerAdapter(context, myMediaControllerCallback);
    }

    @Provides
    MyMediaControllerCallback provideMediaControllerCallback(MyMetaDataCallback myMetaDataCallback,
                                                             MyPlaybackStateCallback myPlaybackStateCallback) {
        return new MyMediaControllerCallback(myMetaDataCallback, myPlaybackStateCallback);
    }

    @Provides
    MyMetaDataCallback provideMetadataCallback(Looper looper) {
        return new MyMetaDataCallback(looper);
    }

    @Provides
    MyPlaybackStateCallback providePlaybackStateCallback(Looper looper) {
        return new MyPlaybackStateCallback(looper);
    }
}
