package com.github.goldy1992.mp3player.dagger.modules;

import com.github.goldy1992.mp3player.client.MyDrawerListener;

import dagger.Module;
import dagger.Provides;

@Module
public class MyDrawerListenerModule {

    @Provides
    public MyDrawerListener provideMyDrawerListener() {
        return new MyDrawerListener();
    }
}
