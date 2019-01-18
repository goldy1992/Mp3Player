package com.example.mike.mp3player.client.view.fragments;

import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.utils.TimerUtils;
import com.example.mike.mp3player.client.view.SeekerBar;
import com.example.mike.mp3player.client.view.TimeCounter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlaybackTrackerFragment extends Fragment {

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
        this.getSeekerBar().init();
        this.getSeekerBar().setTimeCounter(counter);
        this.duration = view.findViewById(R.id.duration);
    }

    public TimeCounter getCounter() {
        return counter;
    }

    public void setMetaData(MediaMetadataCompat metaData) {
        long duration = metaData.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        getCounter().setDuration(duration);
        this.duration.setText(TimerUtils.formatTime(duration));
        getSeekerBar().getMySeekerMediaControllerCallback().onMetadataChanged(metaData);
    }

    public void setPlaybackState(PlaybackStateCompat state) {
        getCounter().updateState(state);
        getSeekerBar().getMySeekerMediaControllerCallback().onPlaybackStateChanged(state);
    }

    public SeekerBar getSeekerBar() {
        return seekerBar;
    }

    public void setSeekerBar(SeekerBar seekerBar) {
        this.seekerBar = seekerBar;
    }
}
