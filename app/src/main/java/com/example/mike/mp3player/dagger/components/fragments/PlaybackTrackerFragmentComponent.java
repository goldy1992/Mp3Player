package com.example.mike.mp3player.dagger.components.fragments;

import android.content.Context;

import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.callbacks.SeekerBarController2;
import com.example.mike.mp3player.client.views.TimeCounter;
import com.example.mike.mp3player.client.views.fragments.PlaybackTrackerFragment;
import com.example.mike.mp3player.dagger.components.MediaPlayerActivityComponent;
import com.example.mike.mp3player.dagger.modules.MainHandlerModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerAdapterModule;
import com.example.mike.mp3player.dagger.modules.MediaControllerCallbackModule;
import com.example.mike.mp3player.dagger.modules.SeekerBarModule;
import com.example.mike.mp3player.dagger.modules.TimeCounterModule;
import com.example.mike.mp3player.dagger.modules.service.HandlerThreadModule;

import javax.inject.Scope;

import dagger.BindsInstance;
import dagger.Component;

@Component(dependencies = {MediaPlayerActivityComponent.class}, modules = {
        HandlerThreadModule.class,
        MediaControllerAdapterModule.class,
        MediaControllerCallbackModule.class,
        SeekerBarModule.class,
        TimeCounterModule.class})
public interface PlaybackTrackerFragmentComponent {

    SeekerBarController2 provideSeekerBarController2();
    TimeCounter provideTimeCounter();

    void inject(PlaybackTrackerFragment fragment);

    @Component.Factory
    public interface Factory {
        PlaybackTrackerFragmentComponent create(@BindsInstance Context context);
    }


}
