package com.github.goldy1992.mp3player.client.views.fragments.viewpager;

import android.content.Intent;
import android.support.v4.media.MediaBrowserCompat;

import com.github.goldy1992.mp3player.commons.MediaItemType;
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent;

import static com.github.goldy1992.mp3player.commons.Constants.MEDIA_ITEM;

public class FolderListFragment extends MediaItemListFragment {

    public static FolderListFragment newInstance(MediaItemType mediaItemType, String id, MediaActivityCompatComponent component) {
        FolderListFragment folderListFragment = new FolderListFragment();
        folderListFragment.init(mediaItemType, id, component);
        return folderListFragment;
    }
    
    @Override
    public void itemSelected(MediaBrowserCompat.MediaItem item) {
        Intent intent = intentMapper.getIntent(this.parentItemType);
        if (null != intent) {
            intent.putExtra(MEDIA_ITEM, item);
            startActivity(intent);
        }
    }
}
