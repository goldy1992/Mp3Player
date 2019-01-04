package com.example.mike.mp3player.client.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.view.ClickableSurfaceFragment;
import com.example.mike.mp3player.client.view.PlayPauseButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

public class PlaybackToolbarExtendedFragment extends PlayToolBarFragment {

    //protected ImageButton
    protected ImageButton skipToNextButton;

    private ClickableSurfaceFragment<AppCompatImageButton> skipToPreviousButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_playback_toolbar_extended, null);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        this.skipToPreviousButton = (ClickableSurfaceFragment) getChildFragmentManager().findFragmentById(R.id.skip_to_previous);
        this.skipToPreviousButton.setOnClickListener((View v) -> skipToPrevious());
        getLayoutInflater(R.layout.button_play_pause, ).
        this.playPauseButton = view.findViewById(R.id.playPauseButton);
        view.a
        this.playPauseButton.setOnClickListener((View v) -> playPause());



        this.skipToNextButton = view.findViewById(R.id.skip_to_next);
        this.skipToNextButton.setOnClickListener((View v) -> skipToNext());
    }

    public void skipToNext() {
        mediaPlayerActionListener.skipToNext();
    }

    public void skipToPrevious() {
        mediaPlayerActionListener.skipToPrevious();
    }
}
