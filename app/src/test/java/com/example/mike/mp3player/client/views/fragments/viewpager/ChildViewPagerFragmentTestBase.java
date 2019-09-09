package com.example.mike.mp3player.client.views.fragments.viewpager;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.views.fragments.FragmentTestBase;
import com.example.mike.mp3player.commons.MediaItemType;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ChildViewPagerFragmentTestBase<T extends  ChildViewPagerFragment> extends FragmentTestBase<T> {

    @Mock
    private MediaBrowserAdapter mediaBrowserAdapter;


    public void setup(Class<T> classType) {
        MockitoAnnotations.initMocks(this);
        super.setup(classType, false);
        ChildViewPagerFragment childViewPagerFragment =
        ((ChildViewPagerFragment)fragment);
        childViewPagerFragment.init(MediaItemType.FOLDERS, "");
        childViewPagerFragment.setMediaBrowserAdapter(mediaBrowserAdapter);
        super.addFragmentToActivity();
    }



}