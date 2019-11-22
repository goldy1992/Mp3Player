package com.github.goldy1992.mp3player.client.views.fragments.viewpager;

import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.commons.MediaItemUtils;
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent;

import javax.inject.Inject;

public class SongListFragment extends MediaItemListFragment {

    @Inject
    public SongListFragment(MediaItemType mediaItemType, String id, MediaActivityCompatComponent compatComponent) {
        super(mediaItemType, id, compatComponent);
    }

    @Override
    public void itemSelected(MediaBrowserCompat.MediaItem item) {
        mediaControllerAdapter.playFromMediaId(MediaItemUtils.getLibraryId(item), null);
    }
}
