package com.github.goldy1992.mp3player.dagger.components.fragments;

import com.github.goldy1992.mp3player.client.MyGenericItemTouchListener;
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment;
import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.dagger.modules.MyRecycleViewModule;

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
