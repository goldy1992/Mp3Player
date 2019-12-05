package com.github.goldy1992.mp3player.client.dagger.components.fragments;

import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope;
import com.github.goldy1992.mp3player.client.views.fragments.MediaControlsFragment;
import com.github.goldy1992.mp3player.client.views.fragments.PlayToolbarFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface PlaybackButtonsSubComponent {

    void inject(MediaControlsFragment mediaControlsFragment);
    void inject(PlayToolbarFragment playToolBarFragment);
}