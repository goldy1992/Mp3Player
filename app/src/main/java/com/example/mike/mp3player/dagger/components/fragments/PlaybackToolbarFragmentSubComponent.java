package com.example.mike.mp3player.dagger.components.fragments;

import com.example.mike.mp3player.client.views.fragments.PlayToolBarFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackToolbarExtendedFragment;
import com.example.mike.mp3player.dagger.modules.PlaybackToolbarButtonsModule;
import com.example.mike.mp3player.dagger.scopes.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = PlaybackToolbarButtonsModule.class)
public interface PlaybackToolbarFragmentSubComponent {

    void inject(PlayToolBarFragment playToolBarFragment);
    void inject(PlaybackToolbarExtendedFragment playbackToolbarExtendedFragment);
}
