package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.dagger.components.MediaPlaybackServiceSubcomponent;
import com.example.mike.mp3player.service.MediaPlaybackService;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module(subcomponents = MediaPlaybackServiceSubcomponent.class)
public abstract class MediaPlaybackServiceModule {

    @Binds
    @IntoMap
    @ClassKey(MediaPlaybackService.class)
    abstract AndroidInjector.Builder<?> provideMediaPlaybackService(
            MediaPlaybackServiceSubcomponent.Factory factory);

}
