package com.example.mike.mp3player.client.view;

import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mp3player.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class PlayToolBarFragment extends Fragment {
    private MediaPlayerActionListener mediaPlayerActionListener;
    private PlayPauseButton playPauseButton;
    private Toolbar toolbar;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_play_toolbar, null);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        toolbar = view.findViewById(R.id.playToolbar);
        toolbar.setOnClickListener((View v) -> mediaPlayerActionListener.goToMediaPlayerActivity());
        playPauseButton = view.findViewById(R.id.mainActivityPlayPauseButton);
        playPauseButton.setOnClickListener((View v) -> playPause(v));
    }

    public void playPause(View view) {
        int currentPlaybackState = playPauseButton.getState();
        if (currentPlaybackState == PlaybackStateCompat.STATE_PLAYING) {
            mediaPlayerActionListener.pause();
            getPlayPauseButton().setPlayIcon();
        } else {
            mediaPlayerActionListener.play();
            getPlayPauseButton().setPauseIcon();
        }
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
