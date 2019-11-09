package com.github.goldy1992.mp3player.dagger.components.fragments;

import com.github.goldy1992.mp3player.client.views.fragments.PlaybackTrackerFragment;
import com.github.goldy1992.mp3player.dagger.modules.SeekerBarModule;
import com.github.goldy1992.mp3player.dagger.modules.TimeCounterModule;
import com.github.goldy1992.mp3player.dagger.scopes.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent( modules = {
        SeekerBarModule.class,
        TimeCounterModule.class})
public interface PlaybackTrackerFragmentSubcomponent {

    void inject(PlaybackTrackerFragment fragment);
}
