package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.views.RepeatOneRepeatAllButton;
import com.example.mike.mp3player.client.views.ShuffleButton;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShuffleRepeatFragment extends AsyncFragment {
    private static final String LOG_TAG = "PLY_PAUSE_BTN";
    MediaControllerAdapter mediaControllerAdapter;
    RepeatOneRepeatAllButton repeatOneRepeatAllButton;
    ShuffleButton shuffleButton;
    boolean attachToRoot;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_shuffle_repeat, container, attachToRoot );
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        repeatOneRepeatAllButton = view.findViewById(R.id.repeatOneRepeatAllButton);
        repeatOneRepeatAllButton.updateState(PlaybackStateCompat.REPEAT_MODE_ALL);
        repeatOneRepeatAllButton.setOnClickListener((View v) -> setRepeatOneRepeatAllButtonMode(v));
        shuffleButton = view.findViewById(R.id.shuffleButton);
        shuffleButton.updateState(PlaybackStateCompat.SHUFFLE_MODE_NONE);
    }

    public void init(MediaControllerAdapter mediaControllerAdapter, boolean attachToRoot) {
        this.mediaControllerAdapter = mediaControllerAdapter;
        if (this.repeatOneRepeatAllButton == null) {
            this.repeatOneRepeatAllButton = RepeatOneRepeatAllButton.create(mediaControllerAdapter.getContext());
        }
        this.mediaControllerAdapter.registerPlaybackStateListener(repeatOneRepeatAllButton, Collections.singleton(ListenerType.REPEAT));
        this.attachToRoot = attachToRoot;
    }

    void setRepeatOneRepeatAllButtonMode(View view) {

        int nextState = repeatOneRepeatAllButton.getNextState();
        // TODO: activate statement when MediaPlaybackService is ready to consume repeat requests
        mediaControllerAdapter.setRepeatMode(nextState);
        repeatOneRepeatAllButton.updateState(repeatOneRepeatAllButton.getNextState());
    }


    public void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    public static ShuffleRepeatFragment createAndInitialiseShuffleRepeatFragment(MediaControllerAdapter mediaControllerAdapter, boolean attachToRoot) {
        ShuffleRepeatFragment shuffleRepeatFragment = new ShuffleRepeatFragment();
        shuffleRepeatFragment.init(mediaControllerAdapter, attachToRoot);
        return shuffleRepeatFragment;
    }


}
