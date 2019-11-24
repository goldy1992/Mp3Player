package com.github.goldy1992.mp3player.dagger.modules;

import android.content.Context;

import com.github.goldy1992.mp3player.client.MediaControllerAdapter;
import com.github.goldy1992.mp3player.client.MockMediaControllerAdapter;
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback;

import dagger.Module;
import dagger.Provides;

@Module
public class MockMediaControllerAdapterModule  {


    @Provides
    MediaControllerAdapter provideMediaControllerAdapter(Context context, MyMediaControllerCallback myMediaControllerCallback) {
        return new MockMediaControllerAdapter(context, myMediaControllerCallback);
    }
}
