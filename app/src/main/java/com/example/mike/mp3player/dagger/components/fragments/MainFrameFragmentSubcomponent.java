package com.example.mike.mp3player.dagger.components.fragments;

import com.example.mike.mp3player.client.views.fragments.MainFrameFragment;
import com.example.mike.mp3player.dagger.modules.ChildViewPagerFragmentModule;
import com.example.mike.mp3player.dagger.modules.MyDrawerListenerModule;
import com.example.mike.mp3player.dagger.scopes.FragmentScope;

import dagger.Subcomponent;

@FragmentScope
@Subcomponent(modules = { ChildViewPagerFragmentModule.class,
        MyDrawerListenerModule.class})
public interface MainFrameFragmentSubcomponent {

    void inject(MainFrameFragment mainFrameFragment);
}
