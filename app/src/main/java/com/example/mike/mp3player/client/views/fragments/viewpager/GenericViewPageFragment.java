package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MyGenericItemTouchListener;
import com.example.mike.mp3player.client.views.MyRecyclerView;
import com.example.mike.mp3player.commons.ComparatorUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public abstract class GenericViewPageFragment extends Fragment implements MyGenericItemTouchListener.ItemSelectedListener {
    private static final String LOG_TAG = "GENRC_VIW_PGE_FRGMNT";
    /**
     * The parent for all the media items in this view; if null, the fragment represent a list of all available songs.
     */
    LibraryRequest parent;
    Category category;
    MyRecyclerView recyclerView;
    Class<?> activityToCall;
    Context context;

    /* TODO: add mechanism to store children in the fragment without having to repoll for the same data */
    Map<MediaItem, List<MediaItem>> songs;
    private MediaBrowserAdapter mediaBrowserAdapter;
    MediaControllerAdapter mediaControllerAdapter;


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_view_page, container, false);
        view.setTag(category);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.recyclerView = view.findViewById(R.id.myRecyclerView);
        this.getRecyclerView().initRecyclerView(category, new ArrayList<>(songs.keySet()), getMediaBrowserAdapter(),
                mediaControllerAdapter, this);
    }

    public void init(Category category, List<MediaItem> songs, Class<?> activityToCall) {
        this.songs = new TreeMap<>(ComparatorUtils.compareMediaItemsByTitle);
        if (null != songs) {
            for (MediaItem m : songs) {
                this.songs.put(m, null);
            }
        }
        this.category = category;
        this.activityToCall = activityToCall;

    }

    public void populatePlaybackMetaDataListeners(MediaBrowserAdapter mediaBrowserAdapter, MediaControllerAdapter mediaControllerAdapter) {
        this.mediaBrowserAdapter = mediaBrowserAdapter;
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.context = mediaBrowserAdapter.getContext();
        this.recyclerView.populatePlaybackMetaDataListeners(mediaBrowserAdapter, mediaControllerAdapter);
    }

    public void onChildrenLoaded(LibraryRequest libraryRequest, @NonNull ArrayList<MediaItem> children) {
        recyclerView.addData(children);
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

    public MediaBrowserAdapter getMediaBrowserAdapter() {
        return mediaBrowserAdapter;
    }
}
