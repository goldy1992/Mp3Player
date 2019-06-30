package com.example.mike.mp3player.dagger.components.fragments;

import com.example.mike.mp3player.client.views.TimeCounter;
import com.example.mike.mp3player.client.views.fragments.PlaybackTrackerFragment;
import com.example.mike.mp3player.dagger.modules.MainHandlerModule;
import com.example.mike.mp3player.dagger.modules.TimeCounterModule;

import dagger.Component;

@Component(modules = {
        MainHandlerModule.class,
        TimeCounterModule.class})
public interface PlaybackTrackerFragmentComponent {

    TimeCounter provideTimeCounter();

    void inject(PlaybackTrackerFragment fragment);


}
