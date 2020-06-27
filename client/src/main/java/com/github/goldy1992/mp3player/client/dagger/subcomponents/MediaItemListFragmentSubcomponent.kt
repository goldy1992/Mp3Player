package com.github.goldy1992.mp3player.client.dagger.subcomponents

import com.github.goldy1992.mp3player.client.dagger.scopes.FragmentScope
import com.github.goldy1992.mp3player.client.listeners.MyGenericItemTouchListener
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.FolderListFragment
import com.github.goldy1992.mp3player.client.views.fragments.viewpager.SongListFragment
import com.github.goldy1992.mp3player.commons.MediaItemType
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface MediaItemListFragmentSubcomponent {

    fun inject(songListFragment: SongListFragment)
    fun inject(folderListFragment : FolderListFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance listener: MyGenericItemTouchListener.ItemSelectedListener,
                   @BindsInstance mediaItemType: MediaItemType,
                   @BindsInstance parentMediaItemId : String)
                : MediaItemListFragmentSubcomponent
    }
}