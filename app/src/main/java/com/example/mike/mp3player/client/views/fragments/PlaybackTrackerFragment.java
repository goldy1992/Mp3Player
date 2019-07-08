package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MetaDataListener;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.callbacks.SeekerBarController2;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.client.views.SeekerBar;
import com.example.mike.mp3player.client.views.TimeCounter;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

public class PlaybackTrackerFragment extends AsyncFragment implements PlaybackStateListener, MetaDataListener {

    private MediaControllerAdapter mediaControllerAdapter;
    private TextView duration;
    private SeekerBarController2 seekerBarController;
    private TimeCounter counter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initialiseDependencies();
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_playback_tracker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        TextView counterView = view.findViewById(R.id.timer);
        this.counter.setTextView(counterView);
        SeekerBar seekerBar = view.findViewById(R.id.seekBar);

       // this.seekerBarController.se(counter);
        this.duration = view.findViewById(R.id.duration);
    }

    public void init(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
        Set<ListenerType> listenForSet = new HashSet<>();
        listenForSet.add(ListenerType.PLAYBACK);
        listenForSet.add(ListenerType.REPEAT);
        this.mediaControllerAdapter.registerPlaybackStateListener(this, listenForSet);
        this.mediaControllerAdapter.registerMetaDataListener(this);
       // this.getSeekerBarController().init(mediaControllerAdapter);
    }

    public TimeCounter getCounter() {
        return counter;
    }

    public SeekerBarController2 getSeekerBarController() {
        return seekerBarController;
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        counter.updateState(state);
        seekerBarController.onPlaybackStateChanged(state);
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        long duration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        getCounter().setDuration(duration);
        String durationString = TimerUtils.formatTime(duration);
        mainUpdater.post(() -> updateDurationText(durationString));
        seekerBarController.onMetadataChanged(metadata);
    }

    public void initialiseDependencies() {
       MediaPlayerActivity mediaPlayerActivity = (MediaPlayerActivity) getActivity();
       mediaPlayerActivity.getMediaPlayerActivityComponent().providePlaybackTrackerFragment().create(getContext()).inject(this);
    }

    private void updateDurationText(String duration) {
        this.duration.setText(duration);
    }

    @Inject
    public void setTimeCounter(TimeCounter timeCounter) {
        this.counter = timeCounter;
    }

    @Inject
    public void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }


    @Inject
    public void setSeekerBarController(SeekerBarController2 controller) {
        this.seekerBarController = controller;
    }
}
