package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Intent;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.utils.IntentUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryConstructor;
import com.example.mike.mp3player.commons.library.LibraryId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public class SongViewPageFragment extends GenericViewPageFragment implements MyGenericItemTouchListener.ItemSelectedListener {

    private static final String LOG_TAG = "VIEW_PAGE_FRAGMENT";
    /**
     * The parent f all the media items in this view; if null, the fragment represent a list of all available songs.
     */
    private LibraryId parent;

    public SongViewPageFragment() {  }

    public void init(LibraryId libraryId, List<MediaItem> songs, MediaBrowserAdapter mediaBrowserAdapter,
                     MediaControllerAdapter mediaControllerAdapter) {
        super.init(Category.SONGS, songs, mediaBrowserAdapter, mediaControllerAdapter, MediaPlayerActivity.class);
        this.parent = libraryId;
        this.songs = new HashMap<>();
        for (MediaItem m : songs) {
            this.songs.put(m, null);
        }
    }


    public static SongViewPageFragment createAndInitialiseViewPageFragment(LibraryId libraryId, List<MediaItem> songs, MediaBrowserAdapter mediaBrowserAdapter,
                                                                           MediaControllerAdapter mediaControllerAdapter) {
        SongViewPageFragment viewPageFragment = new SongViewPageFragment();
        viewPageFragment.init(libraryId, songs, mediaBrowserAdapter, mediaControllerAdapter);
        return viewPageFragment;
    }

    /**
     * @deprecated must specify category in order to know which type of request to build
     * @param songs
     * @param mediaBrowserAdapter
     * @param mediaControllerAdapter
     * @return
     */
    @Deprecated
    public static SongViewPageFragment createAndInitialiseViewPageFragment(List<MediaItem> songs, MediaBrowserAdapter mediaBrowserAdapter,
                                                                           MediaControllerAdapter mediaControllerAdapter) {
        SongViewPageFragment viewPageFragment = new SongViewPageFragment();
        viewPageFragment.init(Category.SONGS, songs, mediaBrowserAdapter, mediaControllerAdapter, MediaPlayerActivity.class);
        return viewPageFragment;
    }

    @Override
    public void itemSelected(String songId) {
        String mediaId = LibraryConstructor.buildSongId(parent, songId);
        Intent intent =
            IntentUtils.createMediaPlayerActivityMediaRequestIntent(context,
                    getMediaBrowserAdapter().getMediaSessionToken(),mediaId);

        startActivity(intent);
    }

    @Override
    public void onChildrenLoaded(LibraryId libraryId, @NonNull ArrayList<MediaItem> children) {

    }
}

