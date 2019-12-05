package com.github.goldy1992.mp3player.dagger.components.fragments;

import com.github.goldy1992.mp3player.client.views.fragments.PlaybackTrackerFragment;
import com.github.goldy1992.mp3player.dagger.scopes.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent
public interface PlaybackTrackerFragmentSubcomponent {

    void inject(PlaybackTrackerFragment fragment);
}
