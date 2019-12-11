package com.github.goldy1992.mp3player.client.dagger.components.fragments

import com.github.goldy1992.mp3player.client.MyGenericItemTouchListener.ItemSelectedListener
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment
import com.github.goldy1992.mp3player.commons.MediaItemType
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent
interface ChildViewPagerFragmentSubcomponent {
    fun inject(mediaItemListFragment: MediaItemListFragment)
    @Subcomponent.Factory
    interface Factory {
        fun create(
                @BindsInstance mediaItemType: MediaItemType,
                @BindsInstance parentId: String,
                @BindsInstance listener: ItemSelectedListener): ChildViewPagerFragmentSubcomponent
    }
}