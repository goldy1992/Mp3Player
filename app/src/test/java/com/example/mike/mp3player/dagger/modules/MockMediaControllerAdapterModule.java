package com.example.mike.mp3player.dagger.modules;

import android.content.Context;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MockMediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.MyMediaControllerCallback;

import dagger.Module;
import dagger.Provides;

@Module
public class MockMediaControllerAdapterModule  {


    @Provides
    MediaControllerAdapter provideMediaControllerAdapter(Context context, MyMediaControllerCallback myMediaControllerCallback) {
        return new MockMediaControllerAdapter(context, myMediaControllerCallback);
    }
}
