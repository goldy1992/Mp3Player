package com.example.mike.mp3player.client.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.utils.IntentUtils;
import com.example.mike.mp3player.client.views.PlayPauseButton;

import java.util.Collections;

import javax.inject.Inject;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class PlayToolBarFragment extends Fragment {

    private static final String LOG_TAG = "PLY_PAUSE_BTN";

    PlayPauseButton playPauseButton;

    protected Toolbar toolbar;
    LinearLayout innerPlaybackToolbarLayout;
    boolean attachToRoot;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initialiseDependencies();
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

        if (null != innerPlaybackToolbarLayout) {
            playPauseButton.setLayoutParams(getLinearLayoutParams(playPauseButton.getLayoutParams()));
            innerPlaybackToolbarLayout.addView(playPauseButton);
        }

    }

    LinearLayout.LayoutParams getLinearLayoutParams(ViewGroup.LayoutParams params) {
        if (params != null) {
            return new LinearLayout.LayoutParams(params.width,
                    params.height, 1.0f);
        }
        return new LinearLayout.LayoutParams(WRAP_CONTENT,
                WRAP_CONTENT, 1.0f);
    }

    @Inject
    public void setPlayPauseButton(PlayPauseButton playPauseButton) {
        this.playPauseButton = playPauseButton;
    }

    private boolean isMediaPlayerActivity() {
        Activity activity = getActivity();
        return activity != null && activity instanceof MediaPlayerActivity;
    }

    private void goToMediaPlayerActivity() {
        Intent intent = IntentUtils.createGoToMediaPlayerActivity(getContext());
        startActivity(intent);
    }

    protected void initialiseDependencies() {
        FragmentActivity activity = getActivity();
        if (null != activity && activity instanceof MediaActivityCompat) {
            MediaActivityCompat mediaPlayerActivity = (MediaActivityCompat) getActivity();
            mediaPlayerActivity.getMediaActivityCompatComponent().playbackToolbarSubcomponent()
                    .inject(this);
        }
    }
}
