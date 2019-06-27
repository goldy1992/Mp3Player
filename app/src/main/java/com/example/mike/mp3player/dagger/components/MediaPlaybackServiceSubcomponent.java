package com.example.mike.mp3player.dagger.components;

import com.example.mike.mp3player.service.MediaPlaybackService;

import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Subcomponent(modules = AndroidInjectionModule.class)
public interface MediaPlaybackServiceSubcomponent extends AndroidInjector<MediaPlaybackService> {

    @Subcomponent.Factory
    public interface Factory extends AndroidInjector.Factory<MediaPlaybackService> {}
}
