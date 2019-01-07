package com.example.mike.mp3player.client.view.fragments;

import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.view.LinearLayoutWithImageView;
import com.example.mike.mp3player.client.view.MediaPlayerActionListener;
import com.example.mike.mp3player.client.view.PlayPauseButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class PlayToolBarFragment extends Fragment {

    protected MediaPlayerActionListener mediaPlayerActionListener;
    PlayPauseButton playPauseButton;
    protected Toolbar toolbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playback_toolbar, null );
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        toolbar = view.findViewById(R.id.playbackToolbar);
        toolbar.setOnClickListener((View v) -> mediaPlayerActionListener.goToMediaPlayerActivity());
        playPauseButton = view.findViewById(R.id.playPauseButton);
        playPauseButton.setOnClickListener((View v) -> playPause());
        initButton(playPauseButton);
    }

    void playPause() {
        int currentPlaybackState = playPauseButton.getState();
        if (currentPlaybackState == PlaybackStateCompat.STATE_PLAYING) {
            mediaPlayerActionListener.pause();
            getPlayPauseButton().setPlayIcon();
        } else {
            mediaPlayerActionListener.play();
            getPlayPauseButton().setPauseIcon();
        }
    }
    
    void initButton(LinearLayoutWithImageView layout) {
        layout.setScaleX(2);
        layout.setScaleY(2);
        int layoutHeight = layout.getHeight();
        int imageHeight = layout.getView().getHeight();
        long exactMarginSize = (layoutHeight - imageHeight) / 2;
        int marginSize =  (int) exactMarginSize;
        layout.setPadding(marginSize, 0, marginSize, 0);
        layout.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_orange_light, null));
    }

    public PlayPauseButton getPlayPauseButton() {
        return playPauseButton;
    }

    public void setPlayPauseButton(PlayPauseButton playPauseButton) {
        this.playPauseButton = playPauseButton;
    }

    public void setMediaPlayerActionListener(MediaPlayerActionListener mediaPlayerActionListener) {
        this.mediaPlayerActionListener = mediaPlayerActionListener;
    }
}
