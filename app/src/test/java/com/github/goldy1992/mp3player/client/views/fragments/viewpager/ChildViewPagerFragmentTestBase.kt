package com.github.goldy1992.mp3player.client.views.fragments.viewpager

import com.github.goldy1992.mp3player.client.MediaBrowserAdapter
import com.github.goldy1992.mp3player.client.views.fragments.FragmentTestBase
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ChildViewPagerFragmentTestBase<T : MediaItemListFragment> : FragmentTestBase<T>() {

    @Mock
    private val mediaBrowserAdapter: MediaBrowserAdapter? = null

    fun setup(classType: Class<T>) {
        MockitoAnnotations.initMocks(this)
        super.setup(classType, false)
        val mediaItemListFragment = fragment as MediaItemListFragment
        //        mediaItemListFragment.init(MediaItemType.FOLDERS, "");
        mediaItemListFragment.mediaBrowserAdapter = mediaBrowserAdapter!!
        super.addFragmentToActivity()
    }
}