package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Intent;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.utils.IntentUtils;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import java.util.List;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public class SongViewPageFragment extends GenericViewPageFragment implements MyGenericItemTouchListener.ItemSelectedListener {

    private static final String LOG_TAG = "VIEW_PAGE_FRAGMENT";

    public SongViewPageFragment() {  }

    public void init(LibraryRequest libraryRequest, List<MediaItem> songs) {
        super.init(Category.SONGS, songs, MediaPlayerActivity.class);
        this.parent = libraryRequest;
    }
    public void populatePlaybackMetaDataListeners(MediaBrowserAdapter mediaBrowserAdapter, MediaControllerAdapter mediaControllerAdapter) {

    }


    public static SongViewPageFragment createAndInitialiseViewPageFragment(LibraryRequest libraryRequest, List<MediaItem> songs) {
        SongViewPageFragment viewPageFragment = new SongViewPageFragment();
        viewPageFragment.init(libraryRequest, songs);
        return viewPageFragment;
    }

    /**
     * @deprecated must specify category in order to know which type of request to build
     * @param songs
     * @return
     */
    @Deprecated
    public static SongViewPageFragment createAndInitialiseViewPageFragment(List<MediaItem> songs) {
        SongViewPageFragment viewPageFragment = new SongViewPageFragment();
        viewPageFragment.init(Category.SONGS, songs, MediaPlayerActivity.class);
        return viewPageFragment;
    }

    @Override
    public void itemSelected(MediaItem item) {
        String mediaId = MediaItemUtils.getMediaId(item);
        if (null == mediaId) {
            return;
        }
        Intent intent =
                IntentUtils.createMediaPlayerActivityMediaRequestIntent(context,
                        getMediaBrowserAdapter().getMediaSessionToken(),mediaId, parent);

        startActivity(intent);
    }
}

