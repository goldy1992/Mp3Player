package com.example.mike.mp3player.client.views.fragments.viewpager;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaBrowserAdapter;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.views.MyRecyclerView;
import com.example.mike.mp3player.commons.library.Category;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

@Deprecated
public class ViewPageChildFragment extends Fragment {
    private String title;
    private TextView textView;
    private MyRecyclerView recyclerView;
    private List<MediaBrowserCompat.MediaItem> songs;
    private MediaControllerAdapter mediaControllerAdapter;
    private MediaBrowserAdapter mediaBrowserAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(0, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.recyclerView = view.findViewById(R.id.myRecyclerView);
        //this.textView = view.findViewById(R.id.parentNameTextView);
        this.recyclerView.initRecyclerView(Category.SONGS, songs, mediaBrowserAdapter,
                mediaControllerAdapter, null);
    }

    public void initRecyclerView(List<MediaBrowserCompat.MediaItem> songs, MediaBrowserAdapter mediaBrowserAdapter,
                                 MediaControllerAdapter mediaControllerAdapter) {
        this.songs = songs;
        this.mediaBrowserAdapter = mediaBrowserAdapter;
        this.mediaControllerAdapter = mediaControllerAdapter;
    }
}
