package com.github.goldy1992.mp3player.dagger.modules;

import com.github.goldy1992.mp3player.client.MediaControllerAdapter;
import com.github.goldy1992.mp3player.client.SeekerBarController2;
import com.github.goldy1992.mp3player.client.views.TimeCounter;
import com.github.goldy1992.mp3player.dagger.scopes.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class SeekerBarModule {

    @FragmentScope
    @Provides
    public SeekerBarController2 provideSeekerBarController(MediaControllerAdapter mediaControllerAdapter,
                                                   TimeCounter timeCounter) {
        return new SeekerBarController2(mediaControllerAdapter, timeCounter);
    }
}
