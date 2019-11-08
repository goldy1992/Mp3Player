package com.github.goldy1992.mp3player.client.views.fragments;

import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.widget.AppCompatImageButton;

import com.github.goldy1992.mp3player.R;
import com.github.goldy1992.mp3player.client.MediaControllerAdapter;
import com.github.goldy1992.mp3player.client.activities.MediaActivityCompat;
import com.github.goldy1992.mp3player.client.callbacks.playback.PlaybackStateListener;
import com.github.goldy1992.mp3player.dagger.components.MediaActivityCompatComponent;

import javax.inject.Inject;

import static com.github.goldy1992.mp3player.commons.Constants.DECREASE_PLAYBACK_SPEED;
import static com.github.goldy1992.mp3player.commons.Constants.INCREASE_PLAYBACK_SPEED;

public class PlaybackSpeedControlsFragment extends AsyncFragment implements PlaybackStateListener {

    private TextView playbackSpeed;
    private AppCompatImageButton increasePlaybackSpeedButton;
    private AppCompatImageButton decreasePlaybackSpeedButton;
    private MediaControllerAdapter mediaControllerAdapter;
    private float speed = 1.0f;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initialiseDependencies();
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_playback_speed_controls, container, true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.decreasePlaybackSpeedButton = view.findViewById(R.id.decreasePlaybackSpeed);
        this.decreasePlaybackSpeedButton.setOnClickListener((View v) -> decreasePlaybackSpeed());
        this.increasePlaybackSpeedButton = view.findViewById(R.id.increasePlaybackSpeed);
        this.increasePlaybackSpeedButton.setOnClickListener((View v) -> increasePlaybackSpeed());
        this.playbackSpeed = view.findViewById(R.id.playbackSpeedValue);

        // register listeners
        this.mediaControllerAdapter.registerPlaybackStateListener(this);

        //update GUI
        this.onPlaybackStateChanged(mediaControllerAdapter.getPlaybackStateCompat());
    }

    private void updatePlaybackSpeedText(float speed) {
        Runnable r = () ->  playbackSpeed.setText(getString(R.string.PLAYBACK_SPEED_VALUE, speed));
        this.mainUpdater.post(r);
    }

    @VisibleForTesting
    public void increasePlaybackSpeed() {
        worker.post(() -> {
            Bundle extras = new Bundle();
            mediaControllerAdapter.sendCustomAction(INCREASE_PLAYBACK_SPEED, extras);
        });
    }

    @VisibleForTesting
    public void decreasePlaybackSpeed() {
        worker.post(() -> {
            Bundle extras = new Bundle();
            this.mediaControllerAdapter.sendCustomAction(DECREASE_PLAYBACK_SPEED, extras);
        });
    }


    @Override
    public void onPlaybackStateChanged(@NonNull PlaybackStateCompat state) {
        this.speed = state.getPlaybackSpeed();
        if (speed > 0) {
            updatePlaybackSpeedText(speed);
        }
    }

    public void initialiseDependencies() {
        MediaActivityCompatComponent component = ((MediaActivityCompat)getActivity())
                .getMediaActivityCompatComponent();
            component.inject(this);
    }

    @Inject
    public void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }
}
