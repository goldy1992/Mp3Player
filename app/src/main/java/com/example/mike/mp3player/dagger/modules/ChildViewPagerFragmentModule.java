package com.example.mike.mp3player.dagger.modules;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.views.fragments.viewpager.ChildViewPagerFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class ChildViewPagerFragmentModule {

    @Provides
    public ChildViewPagerFragment providesChildViewPagerFragment(MediaBrowserAdapter mediaBrowserAdapter) {
        ChildViewPagerFragment childViewPagerFragment = new ChildViewPagerFragment();
        childViewPagerFragment.setMediaBrowserAdapter(mediaBrowserAdapter);
        return childViewPagerFragment;
    }
}
