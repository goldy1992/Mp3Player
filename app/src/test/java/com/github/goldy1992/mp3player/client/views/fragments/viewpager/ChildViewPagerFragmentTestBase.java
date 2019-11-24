package com.github.goldy1992.mp3player.client.views.fragments.viewpager;

import com.github.goldy1992.mp3player.client.MediaBrowserAdapter;
import com.github.goldy1992.mp3player.client.views.fragments.FragmentTestBase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ChildViewPagerFragmentTestBase<T extends MediaItemListFragment> extends FragmentTestBase<T> {

    @Mock
    private MediaBrowserAdapter mediaBrowserAdapter;


    public void setup(Class<T> classType) {
        MockitoAnnotations.initMocks(this);
        super.setup(classType, false);
        MediaItemListFragment mediaItemListFragment =
        ((MediaItemListFragment)fragment);
//        mediaItemListFragment.init(MediaItemType.FOLDERS, "");
        mediaItemListFragment.setMediaBrowserAdapter(mediaBrowserAdapter);
        super.addFragmentToActivity();
    }



}