package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MediaPlayerActvityRequester;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.utils.IntentUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public class SongViewPageFragment extends GenericViewPageFragment implements MyGenericItemTouchListener.ItemSelectedListener {

    private static final String LOG_TAG = "VIEW_PAGE_FRAGMENT";

    public SongViewPageFragment() {  }

    public void init(Category category, List<MediaItem> songs, MediaBrowserAdapter mediaBrowserAdapter,
                     MediaControllerAdapter mediaControllerAdapter, MediaPlayerActvityRequester mediaPlayerActvityRequester) {
        super.init(category, songs, mediaBrowserAdapter, mediaControllerAdapter, MediaPlayerActivity.class);
        this.songs = new HashMap<>();
        for (MediaItem m : songs) {
            this.songs.put(m, null);
        }
    }


    public void onChildrenLoaded(@NonNull String parentId, @NonNull ArrayList<MediaItem> children,
                                 @NonNull Bundle options, Context context) {
    }

    public static SongViewPageFragment createAndInitialiseViewPageFragment(Category category, List<MediaItem> songs, MediaBrowserAdapter mediaBrowserAdapter,
                                                                           MediaControllerAdapter mediaControllerAdapter) {
        SongViewPageFragment viewPageFragment = new SongViewPageFragment();
        viewPageFragment.init(category, songs, mediaBrowserAdapter, mediaControllerAdapter, MediaPlayerActivity.class);
        return viewPageFragment;
    }

    @Override
    public void itemSelected(LibraryId id) {
        Intent intent =
            IntentUtils.createMediaPlayerActivityMediaRequestIntent(context,
                    getMediaBrowserAdapter().getMediaSessionToken(),id.getId());

        startActivity(intent);
    }

    @Override
    public void onChildrenLoaded(LibraryId libraryId, @NonNull ArrayList<MediaItem> children) {

    }
}

