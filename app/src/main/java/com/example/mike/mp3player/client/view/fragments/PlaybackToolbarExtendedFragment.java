package com.example.mike.mp3player.client.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.view.LinearLayoutWithImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PlaybackToolbarExtendedFragment extends PlayToolBarFragment {

    protected LinearLayoutWithImageView skipToPreviousButton;
    protected LinearLayoutWithImageView skipToNextButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LinearLayout root = new LinearLayout(getContext());
        return inflater.inflate(R.layout.playback_toolbar_layout, root, true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.skipToPreviousButton = view.findViewById(R.id.skip_to_previous);
        this.skipToPreviousButton.setOnClickListener((View v) -> skipToPrevious());
        initButton(skipToPreviousButton);

        this.skipToNextButton = view.findViewById(R.id.skip_to_next);
        this.skipToNextButton.setOnClickListener((View v) -> skipToNext());
        initButton(skipToNextButton);
    }

    public void skipToNext() {
        mediaPlayerActionListener.skipToNext();
    }

    public void skipToPrevious() {
        mediaPlayerActionListener.skipToPrevious();
    }
}
