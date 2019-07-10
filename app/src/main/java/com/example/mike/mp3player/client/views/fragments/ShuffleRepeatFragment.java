package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.views.RepeatOneRepeatAllButton;
import com.example.mike.mp3player.client.views.ShuffleButton;
import com.example.mike.mp3player.dagger.components.MainActivityComponent;
import com.example.mike.mp3player.dagger.components.fragments.ShuffleRepeatFragmentSubcomponent;

import java.util.Collections;

import javax.inject.Inject;

public class ShuffleRepeatFragment extends AsyncFragment {
    private static final String LOG_TAG = "PLY_PAUSE_BTN";
    private MediaControllerAdapter mediaControllerAdapter;
    private RepeatOneRepeatAllButton repeatOneRepeatAllButton;
    private ShuffleButton shuffleButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initialiseDependencies();
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_shuffle_repeat, container, true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.repeatOneRepeatAllButton = view.findViewById(R.id.repeatOneRepeatAllButton);
        this.repeatOneRepeatAllButton.updateState(PlaybackStateCompat.REPEAT_MODE_ALL);
        this.repeatOneRepeatAllButton.setOnClickListener(this::setRepeatOneRepeatAllButtonMode);
        this.shuffleButton = view.findViewById(R.id.shuffleButton);
        this.shuffleButton.updateState(PlaybackStateCompat.SHUFFLE_MODE_NONE);
        this.shuffleButton.setOnClickListener(this::setShuffleButtonMode);
        this.mediaControllerAdapter.registerPlaybackStateListener(repeatOneRepeatAllButton, Collections.singleton(ListenerType.REPEAT));
        this.mediaControllerAdapter.registerPlaybackStateListener(shuffleButton, Collections.singleton(ListenerType.SHUFFLE));
    }

    private void setRepeatOneRepeatAllButtonMode(View view) {
        int nextState = repeatOneRepeatAllButton.getNextState();
        // TODO: activate statement when MediaPlaybackService is ready to consume repeat requests
        mediaControllerAdapter.setRepeatMode(nextState);
        repeatOneRepeatAllButton.updateState(repeatOneRepeatAllButton.getNextState());
    }

    private void setShuffleButtonMode(View view) {
        @PlaybackStateCompat.ShuffleMode int newShuffleMode = shuffleButton.toggleShuffle();
        mediaControllerAdapter.setShuffleMode(newShuffleMode);
    }

    @Inject
    public void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    public void initialiseDependencies() {
        ShuffleRepeatFragmentSubcomponent component = ((MediaPlayerActivity)getActivity())
                .getMediaPlayerActivityComponent()
                .provideShuffleRepeatFragment();
        component.inject(this);
    }
}
