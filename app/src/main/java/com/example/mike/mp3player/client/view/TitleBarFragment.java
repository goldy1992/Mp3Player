package com.example.mike.mp3player.client.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.mike.mp3player.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class TitleBarFragment extends Fragment {

    private ImageButton searchFilterButton;
    private Toolbar titleToolbar;
    private SongSearchActionListener songSearchActionListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_title_bar, null);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        titleToolbar = view.findViewById(R.id.titleToolbar);
        searchFilterButton = view.findViewById(R.id.searchFilter);
        searchFilterButton.setOnClickListener((View v) -> songSearchActionListener.onStartSearch());
    }

    public Toolbar getTitleToolbar() {
        return titleToolbar;
    }

    public void setSongSearchActionListener(SongSearchActionListener songSearchActionListener) {
        this.songSearchActionListener = songSearchActionListener;
    }
}
