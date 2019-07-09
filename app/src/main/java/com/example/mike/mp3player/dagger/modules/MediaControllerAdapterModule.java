package com.example.mike.mp3player.dagger.modules;

import android.content.Context;
import android.os.Handler;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;
import com.example.mike.mp3player.client.callbacks.MyMetaDataCallback;
import com.example.mike.mp3player.client.callbacks.playback.MyPlaybackStateCallback;
import com.example.mike.mp3player.dagger.scopes.AndroidComponentScope;


import dagger.Module;
import dagger.Provides;

@Module
public class MediaControllerAdapterModule {

    @AndroidComponentScope
    @Provides
    MediaControllerAdapter provideMediaControllerAdapter(Context context, MyMediaControllerCallback myMediaControllerCallback) {
        return new MediaControllerAdapter(context, myMediaControllerCallback);
    }


}
