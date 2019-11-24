package com.github.goldy1992.mp3player.client.views.fragments.viewpager;

import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.commons.MediaItemUtils;
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent;

public class SongListFragment extends MediaItemListFragment {

    public static SongListFragment newInstance(MediaItemType mediaItemType, String id, MediaActivityCompatComponent component) {
        SongListFragment songListFragment = new SongListFragment();
        songListFragment.init(mediaItemType, id, component);
        return songListFragment;
    }
    @Override
    public void itemSelected(MediaBrowserCompat.MediaItem item) {
        mediaControllerAdapter.playFromMediaId(MediaItemUtils.getLibraryId(item), null);
    }
}
