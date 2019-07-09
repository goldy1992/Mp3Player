package com.example.mike.mp3player.dagger.components.fragments;

import android.content.Context;

import com.example.mike.mp3player.client.callbacks.SeekerBarController2;
import com.example.mike.mp3player.client.views.TimeCounter;
import com.example.mike.mp3player.client.views.fragments.PlaybackTrackerFragment;
import com.example.mike.mp3player.dagger.modules.SeekerBarModule;
import com.example.mike.mp3player.dagger.modules.TimeCounterModule;
import com.example.mike.mp3player.dagger.scopes.AndroidComponentScope;
import com.example.mike.mp3player.dagger.scopes.FragmentScope;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Subcomponent;

@FragmentScope
@Subcomponent( modules = {
        SeekerBarModule.class,
        TimeCounterModule.class})
public interface PlaybackTrackerFragmentSubcomponent {

    SeekerBarController2 provideSeekerBarController2();
    TimeCounter provideTimeCounter();

    void inject(PlaybackTrackerFragment fragment);

    @Subcomponent.Factory
    public interface Factory {
        PlaybackTrackerFragmentSubcomponent create(@BindsInstance Context context);
    }


}
