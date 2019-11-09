package com.github.goldy1992.mp3player.dagger.components.fragments;

import com.github.goldy1992.mp3player.client.views.fragments.MediaControlsFragment;
import com.github.goldy1992.mp3player.client.views.fragments.PlayToolBarFragment;
import com.github.goldy1992.mp3player.dagger.modules.PlaybackButtonsModule;
import com.github.goldy1992.mp3player.dagger.scopes.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = PlaybackButtonsModule.class)
public interface PlaybackButtonsSubComponent {

    void inject(MediaControlsFragment mediaControlsFragment);
    void inject(PlayToolBarFragment playToolBarFragment);
}
