package com.github.goldy1992.mp3player.client.dagger.components.fragments;

import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope;
import com.github.goldy1992.mp3player.client.views.fragments.PlaybackTrackerFragment;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface PlaybackTrackerFragmentSubcomponent {

    void inject(PlaybackTrackerFragment fragment);
}
