package com.example.mike.mp3player.client.views.fragments;

import android.content.Context;
import android.os.Bundle;

import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MediaPlayerActvityRequester;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.views.MyRecyclerView;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public class GenericViewPageFragment extends Fragment implements MyGenericItemTouchListener.ItemSelectedListener {
    private static final String LOG_TAG = "GENRC_VIW_PGE_FRGMNT";
    Category category;
    MyRecyclerView recyclerView;
    Class<?> activityToCall;
    Context context;

    /* TODO: add mechanism to store children in the fragment without having to repoll for the same data */
    Map<MediaItem, List<MediaItem>> songs;
    MediaBrowserAdapter mediaBrowserAdapter;
    MediaControllerAdapter mediaControllerAdapter;
    MediaPlayerActvityRequester mediaPlayerActvityRequester;
    String idRequested = null;

    public void init(Category category, List<MediaItem> songs, MediaBrowserAdapter mediaBrowserAdapter,
                     MediaControllerAdapter mediaControllerAdapter, Class<?> activityToCall) {
        this.songs = new HashMap<>();
        for (MediaItem m : songs) {
            this.songs.put(m, null);
        }
        this.category = category;
        this.mediaBrowserAdapter = mediaBrowserAdapter;
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.activityToCall = activityToCall;
    }

    public void onChildrenLoaded(LibraryId libraryId, @NonNull ArrayList<MediaItem> children,
                             @NonNull Bundle options, Context context) {

    }


        public MyRecyclerView getRecyclerView() {
        return recyclerView;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public void itemSelected(String id) {

    }
}
