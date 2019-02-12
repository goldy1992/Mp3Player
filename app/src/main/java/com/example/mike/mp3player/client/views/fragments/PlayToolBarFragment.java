package com.example.mike.mp3player.client.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MediaPlayerActvityRequester;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.utils.IntentUtils;
import com.example.mike.mp3player.client.views.LinearLayoutWithImageView;
import com.example.mike.mp3player.client.views.PlayPauseButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PlayToolBarFragment extends Fragment {

    MediaControllerAdapter mediaControllerAdapter;
    PlayPauseButton playPauseButton;
    protected Toolbar toolbar;
    LinearLayout innerPlaybackToolbarLayout;
    boolean attachToRoot;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_playback_toolbar, container, attachToRoot );
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        toolbar = view.findViewById(R.id.playbackToolbar);
        if (!isMediaPlayerActivity()) {
            toolbar.setOnClickListener((View v) -> goToMediaPlayerActivity());
        }
        this.innerPlaybackToolbarLayout = view.findViewById(R.id.innerPlaybackToolbarLayout);
        if (this.playPauseButton == null) {
            this.playPauseButton = PlayPauseButton.create(getContext());
        }
        this.playPauseButton.getView().setOnClickListener((View v) -> playPause());
        initButton(playPauseButton);
    }

    public void init(MediaControllerAdapter mediaControllerAdapter, boolean attachToRoot) {
        this.mediaControllerAdapter = mediaControllerAdapter;
        if (this.playPauseButton == null) {
            this.playPauseButton = PlayPauseButton.create(mediaControllerAdapter.getContext());
        }
        this.mediaControllerAdapter.registerPlaybackStateListener(playPauseButton);
        this.attachToRoot = attachToRoot;
    }

    void playPause() {
        int currentPlaybackState = playPauseButton.getState();
        if (currentPlaybackState == PlaybackStateCompat.STATE_PLAYING) {
            mediaControllerAdapter.pause();
            getPlayPauseButton().setPlayIcon();
        } else {
            mediaControllerAdapter.play();
            getPlayPauseButton().setPauseIcon();
        }
    }

    void initButton(LinearLayoutWithImageView layout) {
        ImageView imageView = layout.getView();
        imageView.setScaleX(2);
        imageView.setScaleY(2);
        int imageHeight = imageView.getDrawable().getIntrinsicHeight();
        long exactMarginSize =  imageHeight / 2;
        int marginSize =  (int) exactMarginSize;
        imageView.setPadding(marginSize, marginSize, marginSize, marginSize);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        //layout.setBackgroundColor(getContext().getResources().getColor(android.R.color.holo_orange_light, null));

    }

    public void displayButtons() {
        if (null != innerPlaybackToolbarLayout) {
            playPauseButton.setLayoutParams(getLinearLayoutParams(playPauseButton.getLayoutParams()));
            innerPlaybackToolbarLayout.addView(playPauseButton);
        }
    }

    public PlayPauseButton getPlayPauseButton() {
        return playPauseButton;
    }

    public void setPlayPauseButton(PlayPauseButton playPauseButton) {
        this.playPauseButton = playPauseButton;
    }


    LinearLayout.LayoutParams getLinearLayoutParams(ViewGroup.LayoutParams params) {
        if (params != null) {
            return new LinearLayout.LayoutParams(params.width,
                    params.height, 1.0f);
        }
        return new LinearLayout.LayoutParams(WRAP_CONTENT,
                WRAP_CONTENT, 1.0f);
    }

    public void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    public static PlayToolBarFragment createAndInitialisePlayToolbarFragment(MediaControllerAdapter mediaControllerAdapter, boolean attachToRoot) {
        PlayToolBarFragment playToolBarFragment = new PlayToolBarFragment();
        playToolBarFragment.init(mediaControllerAdapter, attachToRoot);
        return playToolBarFragment;
    }

    private boolean isMediaPlayerActivity() {
        Activity activity = getActivity();
        return activity != null && activity instanceof MediaPlayerActivity;
    }

    private void goToMediaPlayerActivity() {
        Intent intent = IntentUtils.createGoToMediaPlayerActivity(getContext(), mediaControllerAdapter.getToken());
        startActivity(intent);
    }
}
