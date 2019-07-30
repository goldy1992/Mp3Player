package com.example.mike.mp3player.client.views.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.activities.MediaPlayerActivity;
import com.example.mike.mp3player.client.utils.IntentUtils;
import com.example.mike.mp3player.client.views.buttons.PlayPauseButton;
import com.example.mike.mp3player.client.views.buttons.SkipToNextButton;
import com.example.mike.mp3player.client.views.buttons.SkipToPreviousButton;

import javax.inject.Inject;

public class PlayToolBarFragment extends Fragment {

    private static final String LOG_TAG = "PLY_PAUSE_BTN";

    private PlayPauseButton playPauseButton;
    private SkipToPreviousButton skipToPreviousButton;
    private SkipToNextButton skipToNextButton;

    protected Toolbar toolbar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initialiseDependencies();
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_playback_toolbar, container, true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        toolbar = view.findViewById(R.id.playbackToolbar);
        this.skipToPreviousButton.init( view.findViewById(R.id.skip_to_previous));
        this.skipToNextButton.init(view.findViewById(R.id.skip_to_next));
        this.playPauseButton.init(view.findViewById(R.id.playPauseButton));
        if (!isMediaPlayerActivity()) {
            toolbar.setOnClickListener((View v) -> goToMediaPlayerActivity());
        }
    }

    @Inject
    public void setPlayPauseButton(PlayPauseButton playPauseButton) {
        this.playPauseButton = playPauseButton;
    }

    @Inject
    public void setSkipToPreviousButton(SkipToPreviousButton skipToPreviousButton) {
        this.skipToPreviousButton = skipToPreviousButton;
    }

    @Inject
    public void setSkipToNextButton(SkipToNextButton skipToNextButton) {
        this.skipToNextButton = skipToNextButton;
    }

    private boolean isMediaPlayerActivity() {
        Activity activity = getActivity();
        return activity != null && activity instanceof MediaPlayerActivity;
    }

    private void goToMediaPlayerActivity() {
        Intent intent = IntentUtils.createGoToMediaPlayerActivity(getContext());
        startActivity(intent);
    }


    void initialiseDependencies() {
        FragmentActivity activity = getActivity();
        if (null != activity && activity instanceof MediaActivityCompat) {
            MediaActivityCompat mediaPlayerActivity = (MediaActivityCompat) getActivity();
            mediaPlayerActivity.getMediaActivityCompatComponent().playbackButtonsSubcomponent()
                    .inject(this);
        }
    }
}
