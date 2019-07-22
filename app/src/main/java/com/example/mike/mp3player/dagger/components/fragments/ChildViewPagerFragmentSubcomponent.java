package com.example.mike.mp3player.dagger.components.fragments;

import android.content.Context;

import com.example.mike.mp3player.client.views.fragments.viewpager.ChildViewPagerFragment;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.dagger.modules.MyViewAdapterModule;

import dagger.BindsInstance;
import dagger.Subcomponent;


@Subcomponent(modules = {MyViewAdapterModule.class})
public interface ChildViewPagerFragmentSubcomponent {

    void inject(ChildViewPagerFragment childViewPagerFragment);

    @Subcomponent.Factory
    public interface Factory {
        ChildViewPagerFragmentSubcomponent create(@BindsInstance Context context,
                                                  @BindsInstance Category category,
                                                  @BindsInstance LibraryObject libraryObject);
    }
}
