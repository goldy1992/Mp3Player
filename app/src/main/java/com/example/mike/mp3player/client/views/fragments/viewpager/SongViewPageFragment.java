package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Intent;

import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.utils.IntentUtils;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import java.util.List;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public class SongViewPageFragment extends GenericViewPageFragment implements MyGenericItemTouchListener.ItemSelectedListener {

    private static final String LOG_TAG = "SNG_VW_PGE_FRGMNT";

    public SongViewPageFragment() {  }

    public void init(List<MediaItem> songs) {
        super.init(Category.SONGS, songs, MediaPlayerActivity.class);
    }

    /**
     * @param songs
     * @return
     */
    public static SongViewPageFragment createAndInitialiseViewPageFragment(Category category, List<MediaItem> songs) {
        SongViewPageFragment viewPageFragment = new SongViewPageFragment();
        viewPageFragment.init(category, songs, MediaPlayerActivity.class);
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
                        getMediaBrowserAdapter().getMediaSessionToken(),mediaId, new LibraryRequest(category, mediaId));

        startActivity(intent);
    }
}

