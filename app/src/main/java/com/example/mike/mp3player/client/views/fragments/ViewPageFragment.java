package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MediaPlayerActvityRequester;
import com.example.mike.mp3player.client.views.MyRecyclerView;
import com.example.mike.mp3player.commons.library.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ViewPageFragment extends Fragment {

    private static final String LOG_TAG = "VIEW_PAGE_FRAGMENT";
    private Category category;
    private MyRecyclerView recyclerView;
    List<MediaBrowserCompat.MediaItem> songs;
    MediaBrowserAdapter mediaBrowserAdapter;
    MediaControllerAdapter mediaControllerAdapter;
    MediaPlayerActvityRequester mediaPlayerActvityRequester;

    public ViewPageFragment() {  }

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
        this.getRecyclerView().initRecyclerView(category, songs, mediaBrowserAdapter,
                mediaControllerAdapter, mediaPlayerActvityRequester);
    }

    public void initRecyclerView(Category category, List<MediaBrowserCompat.MediaItem> songs, MediaBrowserAdapter mediaBrowserAdapter,
                                 MediaControllerAdapter mediaControllerAdapter, MediaPlayerActvityRequester mediaPlayerActvityRequester) {
        this.category = category;
        this.songs = songs;
        this.mediaBrowserAdapter = mediaBrowserAdapter;
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.mediaPlayerActvityRequester = mediaPlayerActvityRequester;
    }

    public void setChildren(int containerId, String id, List<MediaBrowserCompat.MediaItem> children) {
        Log.i(LOG_TAG, "hit set children with id: " + id);


        ViewPageChildFragment viewPageChildFragment = new ViewPageChildFragment();
        viewPageChildFragment.initRecyclerView(children,mediaBrowserAdapter, mediaControllerAdapter, mediaPlayerActvityRequester);
        FragmentTransaction transaction = this.getChildFragmentManager().beginTransaction();
        transaction.replace(containerId, viewPageChildFragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
}
