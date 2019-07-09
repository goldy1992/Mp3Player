package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.SeekerBarController2;
import com.example.mike.mp3player.client.views.TimeCounter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SeekerBarModule {

    @Provides
    public SeekerBarController2 provideSeekerBarController(MediaControllerAdapter mediaControllerAdapter,
                                                   TimeCounter timeCounter) {
        return new SeekerBarController2(mediaControllerAdapter, timeCounter);
    }
}
