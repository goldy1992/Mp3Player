package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.dagger.components.MediaActivityCompatComponent;
import com.google.android.material.appbar.AppBarLayout;

public class SongViewPagerFragment extends ChildViewPagerFragment {

    public SongViewPagerFragment(MediaItemType mediaItemType, String id, MediaActivityCompatComponent compatComponent) {
        super(mediaItemType, id, compatComponent);
    }

    @Override
    public void itemSelected(MediaBrowserCompat.MediaItem item) {
        mediaControllerAdapter.playFromMediaId(MediaItemUtils.getLibraryId(item), null);
    }
}
