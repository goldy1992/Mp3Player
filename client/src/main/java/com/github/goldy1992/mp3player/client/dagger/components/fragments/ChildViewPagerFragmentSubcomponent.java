package com.github.goldy1992.mp3player.client.dagger.components.fragments;

import com.github.goldy1992.mp3player.client.MyGenericItemTouchListener;
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment;
import com.github.goldy1992.mp3player.commons.MediaItemType;

import dagger.BindsInstance;
import dagger.Subcomponent;


@Subcomponent
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
