package com.example.mike.mp3player.dagger.components.fragments;

import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.views.fragments.viewpager.MediaItemListFragment;
import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.dagger.modules.MyRecycleViewModule;

import dagger.BindsInstance;
import dagger.Subcomponent;


@Subcomponent(modules = {MyRecycleViewModule.class})
public interface ChildViewPagerFragmentSubcomponent {

    void inject(MediaItemListFragment mediaItemListFragment);

    @Subcomponent.Factory
    interface Factory {
        ChildViewPagerFragmentSubcomponent create(
          @BindsInstance MediaItemType mediaItemType,
          @BindsInstance String parentId,
          @BindsInstance MyGenericItemTouchListener.ItemSelectedListener listener);
    }
}
