package com.github.goldy1992.mp3player.client.views.fragments;

import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.MediaControllerAdapter;
import com.github.goldy1992.mp3player.client.SeekerBarController2;
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat;
import com.github.goldy1992.mp3player.client.callbacks.metadata.MetadataListener;
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener;
import com.github.goldy1992.mp3player.client.utils.TimerUtils;
import com.github.goldy1992.mp3player.client.views.SeekerBar;
import com.github.goldy1992.mp3player.client.views.TimeCounter;
import com.github.goldy1992.mp3player.dagger.components.fragments.PlaybackTrackerFragmentSubcomponent;

import javax.inject.Inject;

public class PlaybackTrackerFragment extends AsyncFragment implements PlaybackStateListener, MetadataListener {

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
        // init counter
        TextView counterView = view.findViewById(R.id.timer);
        this.counter.init(counterView);

        // init seeker bar
        SeekerBar seekerBar = view.findViewById(R.id.seekBar);
        this.seekerBarController.init(seekerBar);

        // init duration
        this.duration = view.findViewById(R.id.duration);

        // init MediaController listeners
        registerMediaControllerListeners();

        // update GUI state
        onMetadataChanged(mediaControllerAdapter.getMetadata());
        onPlaybackStateChanged(mediaControllerAdapter.getPlaybackStateCompat());
    }

    private void registerMediaControllerListeners() {
        this.mediaControllerAdapter.registerPlaybackStateListener(this);
        this.mediaControllerAdapter.registerMetaDataListener(this);
    }

    @Override
    public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
        counter.updateState(state);
        seekerBarController.onPlaybackStateChanged(state);
    }

    @Override
    public void onMetadataChanged(@NonNull MediaMetadataCompat metadata) {
        long duration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        this.counter.setDuration(duration);
        String durationString = TimerUtils.formatTime(duration);
        mainUpdater.post(() -> updateDurationText(durationString));
        seekerBarController.onMetadataChanged(metadata);
    }

    public void initialiseDependencies() {
        PlaybackTrackerFragmentSubcomponent component = ((MediaActivityCompat)getActivity())
                .getMediaActivityCompatComponent()
                .playbackTrackerSubcomponent();
        component.inject(this);
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
