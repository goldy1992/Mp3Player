package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.MediaItemUtils;

public class SongViewPagerFragment extends ChildViewPagerFragment {
    @Override
    public void itemSelected(MediaBrowserCompat.MediaItem item) {
        mediaControllerAdapter.playFromMediaId(MediaItemUtils.getLibraryId(item), null);
    }
}
