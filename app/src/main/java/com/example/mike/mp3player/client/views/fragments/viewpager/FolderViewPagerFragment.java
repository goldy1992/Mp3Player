package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.dagger.components.MediaActivityCompatComponent;
import com.google.android.material.appbar.AppBarLayout;

import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM;

public class FolderViewPagerFragment extends ChildViewPagerFragment {
    public FolderViewPagerFragment(MediaItemType mediaItemType, String id, MediaActivityCompatComponent component) {
        super(mediaItemType, id, component);
    }

    @Override
    public void itemSelected(MediaBrowserCompat.MediaItem item) {
        Intent intent = new Intent(getContext(), intentClass);
        intent.putExtra(MEDIA_ITEM, item);
        startActivity(intent);
    }
}
