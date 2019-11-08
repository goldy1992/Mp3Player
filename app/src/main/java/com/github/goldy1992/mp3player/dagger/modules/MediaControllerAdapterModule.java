package com.github.goldy1992.mp3player.dagger.modules;

import android.content.Context;

import com.github.goldy1992.mp3player.client.MediaControllerAdapter;
import com.github.goldy1992.mp3player.client.callbacks.MyMediaControllerCallback;
import com.github.goldy1992.mp3player.dagger.scopes.ComponentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaControllerAdapterModule {

    @ComponentScope
    @Provides
    MediaControllerAdapter provideMediaControllerAdapter(Context context, MyMediaControllerCallback myMediaControllerCallback) {
        return new MediaControllerAdapter(context, myMediaControllerCallback);
    }


}
