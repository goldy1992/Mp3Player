package com.example.mike.mp3player.dagger.components.fragments;

import com.example.mike.mp3player.client.callbacks.SeekerBarController2;
import com.example.mike.mp3player.client.views.fragments.PlaybackTrackerFragment;
import com.example.mike.mp3player.dagger.modules.SeekerBarModule;
import com.example.mike.mp3player.dagger.modules.TimeCounterModule;
import com.example.mike.mp3player.dagger.scopes.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent( modules = {
        SeekerBarModule.class,
        TimeCounterModule.class})
public interface PlaybackTrackerFragmentSubcomponent {

    SeekerBarController2 provideSeekerBarController2();

    void inject(PlaybackTrackerFragment fragment);


}
