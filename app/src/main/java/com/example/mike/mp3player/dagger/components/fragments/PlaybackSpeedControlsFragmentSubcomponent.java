package com.example.mike.mp3player.dagger.components.fragments;

import com.example.mike.mp3player.client.views.fragments.PlaybackSpeedControlsFragment;
import com.example.mike.mp3player.dagger.scopes.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface PlaybackSpeedControlsFragmentSubcomponent {

    void inject(PlaybackSpeedControlsFragment fragment);
}
