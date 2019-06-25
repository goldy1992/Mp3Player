package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.dagger.modules.service.ServiceContextModule;
import com.example.mike.mp3player.dagger.modules.service.ServiceModule;
import com.example.mike.mp3player.service.MediaPlaybackService;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MediaPlaybackServiceModule {

    @ContributesAndroidInjector
    abstract MediaPlaybackService provideMediaPlaybackService();

}
