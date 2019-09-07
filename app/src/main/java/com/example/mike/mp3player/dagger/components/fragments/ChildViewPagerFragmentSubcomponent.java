package com.example.mike.mp3player.dagger.components.fragments;

import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.views.fragments.viewpager.ChildViewPagerFragment;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.dagger.modules.MyRecycleViewModule;

import dagger.BindsInstance;
import dagger.Subcomponent;


@Subcomponent(modules = {MyRecycleViewModule.class})
public interface ChildViewPagerFragmentSubcomponent {

    void inject(ChildViewPagerFragment childViewPagerFragment);

    @Subcomponent.Factory
    interface Factory {
        ChildViewPagerFragmentSubcomponent create(
          @BindsInstance MediaItemType mediaItemType,
          @BindsInstance String parentId,
          @BindsInstance MyGenericItemTouchListener.ItemSelectedListener listener);
    }
}
