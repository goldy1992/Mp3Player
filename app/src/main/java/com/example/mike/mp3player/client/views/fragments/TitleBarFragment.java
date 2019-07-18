package com.example.mike.mp3player.client.views.fragments;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.views.SongSearchActionListener;

public class TitleBarFragment extends Fragment {

    private ImageButton searchFilterButton;
    private Toolbar titleToolbar;
    private SongSearchActionListener songSearchActionListener;

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
        //searchFilterButton.setOnClickListener((View v) -> songSearchActionListener.onStartSearch());

        /* TODO: consider different implementation of this functionality */
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();

            activity.setSupportActionBar(titleToolbar);
            ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
    }

    public Toolbar getTitleToolbar() {
        return titleToolbar;
    }

    public void setSongSearchActionListener(SongSearchActionListener songSearchActionListener) {
        this.songSearchActionListener = songSearchActionListener;
    }


}
