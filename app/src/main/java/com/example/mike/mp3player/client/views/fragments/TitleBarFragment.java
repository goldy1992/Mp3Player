package com.example.mike.mp3player.client.views.fragments;

import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.views.SongSearchActionListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class TitleBarFragment extends Fragment {

    private ImageButton searchFilterButton;
    private Toolbar titleToolbar;
    private SongSearchActionListener songSearchActionListener;
    private LinearLayout rootMenuItems;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_title_bar, container, true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        titleToolbar = view.findViewById(R.id.titleToolbar);
        searchFilterButton = view.findViewById(R.id.searchFilter);
        searchFilterButton.setOnClickListener((View v) -> songSearchActionListener.onStartSearch());
        rootMenuItems = view.findViewById(R.id.rootMenuItems);
    }

    public Toolbar getTitleToolbar() {
        return titleToolbar;
    }

    public void setSongSearchActionListener(SongSearchActionListener songSearchActionListener) {
        this.songSearchActionListener = songSearchActionListener;
    }

    public void initRootMenu(List<MediaBrowserCompat.MediaItem> items) {
        for (MediaBrowserCompat.MediaItem i : items) {
            TextView tv = new TextView(getContext(), null);
            tv.setText(i.getDescription().getTitle());
            rootMenuItems.addView(tv);
        }
    }
}
