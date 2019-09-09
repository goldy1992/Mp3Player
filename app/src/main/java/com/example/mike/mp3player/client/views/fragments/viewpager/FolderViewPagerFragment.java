package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;

import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM;

public class FolderViewPagerFragment extends ChildViewPagerFragment {
    @Override
    public void itemSelected(MediaBrowserCompat.MediaItem item) {
        Intent intent = new Intent(getContext(), intentClass);
        intent.putExtra(MEDIA_ITEM, item);
        startActivity(intent);
    }
}
