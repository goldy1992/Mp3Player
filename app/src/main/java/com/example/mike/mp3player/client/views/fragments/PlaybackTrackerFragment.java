package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MetaDataListener;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.client.views.SeekerBar;
import com.example.mike.mp3player.client.views.TimeCounter;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PlaybackTrackerFragment extends AsyncFragment implements PlaybackStateListener, MetaDataListener {

    private MediaControllerAdapter mediaControllerAdapter;
    private TextView duration;
    private SeekerBar seekerBar;
    private TimeCounter counter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_playback_tracker, container, true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        TextView counterView = view.findViewById(R.id.timer);
        this.counter = new TimeCounter( counterView);

        this.setSeekerBar(view.findViewById(R.id.seekBar));
        this.getSeekerBar().setTimeCounter(counter);
        this.duration = view.findViewById(R.id.duration);
    }

    public void init(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
        Set<ListenerType> listenForSet = new HashSet();
        listenForSet.add(ListenerType.PLAYBACK);
        listenForSet.add(ListenerType.REPEAT);
        this.mediaControllerAdapter.registerPlaybackStateListener(this, listenForSet);
        this.mediaControllerAdapter.registerMetaDataListener(this);
        this.getSeekerBar().init(mediaControllerAdapter);

    }

    public TimeCounter getCounter() {
        return counter;
    }

    public SeekerBar getSeekerBar() {
        return seekerBar;
    }

    public void setSeekerBar(SeekerBar seekerBar) {
        this.seekerBar = seekerBar;
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        getCounter().updateState(state);
        getSeekerBar().getSeekerBarController().onPlaybackStateChanged(state);
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        long duration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        getCounter().setDuration(duration);
        String durationString = TimerUtils.formatTime(duration);
        mainUpdater.post(() -> updateDurationText(durationString));
        getSeekerBar().getSeekerBarController().onMetadataChanged(metadata);
    }

    private void updateDurationText(String duration) {
        this.duration.setText(duration);
    }
}
