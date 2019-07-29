package com.example.mike.mp3player.dagger.components.fragments;

import com.example.mike.mp3player.client.views.fragments.MediaControlsFragment;
import com.example.mike.mp3player.client.views.fragments.PlayToolBarFragment;
import com.example.mike.mp3player.client.views.fragments.PlaybackToolbarExtendedFragment;
import com.example.mike.mp3player.dagger.modules.PlaybackButtonsModule;
import com.example.mike.mp3player.dagger.scopes.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = PlaybackButtonsModule.class)
public interface PlaybackButtonsSubComponent {

    void inject(MediaControlsFragment mediaControlsFragment);
    void inject(PlayToolBarFragment playToolBarFragment);
    void inject(PlaybackToolbarExtendedFragment playbackToolbarExtendedFragment);
}
