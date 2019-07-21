package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.client.MyDrawerListener;

import dagger.Module;
import dagger.Provides;

@Module
public class MyDrawerListenerModule {

    @Provides
    public MyDrawerListener provideMyDrawerListener() {
        return new MyDrawerListener();
    }
}
