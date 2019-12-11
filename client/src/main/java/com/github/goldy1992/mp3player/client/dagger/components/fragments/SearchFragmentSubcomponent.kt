package com.github.goldy1992.mp3player.client.dagger.components.fragments

import dagger.Subcomponent
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.MediaItemListFragment
import dagger.BindsInstance
import com.github.goldy1992.mp3player.commons.MediaItemType
import com.github.goldy1992.mp3player.client.MyGenericItemTouchListener.ItemSelectedListener
import com.github.goldy1992.mp3player.client.dagger.components.fragments.ChildViewPagerFragmentSubcomponent
import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import com.github.goldy1992.mp3player.client.views.fragments.MediaControlsFragment
import com.github.goldy1992.mp3player.client.views.fragments.PlayToolbarFragment
import com.github.goldy1992.mp3player.client.views.fragments.PlaybackTrackerFragment
import com.github.goldy1992.mp3player.client.views.fragments.SearchFragment

@FragmentScope
@Subcomponent
interface SearchFragmentSubcomponent {
    fun inject(fragment: SearchFragment?)
}