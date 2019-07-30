package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.views.buttons.PlayPauseButton;
import com.example.mike.mp3player.client.views.buttons.SkipToNextButton;
import com.example.mike.mp3player.client.views.buttons.SkipToPreviousButton;


@Deprecated
public class PlaybackToolbarExtendedFragment extends PlayToolBarFragment {

    protected PlayPauseButton playPauseButton;
    protected SkipToPreviousButton skipToPreviousButton;
    protected SkipToNextButton skipToNextButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initialiseDependencies();
        super.onCreateView(inflater, container, savedInstanceState);
        LinearLayout root = new LinearLayout(getContext());
        return inflater.inflate(R.layout.fragment_playback_toolbar, root, true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (null != innerPlaybackToolbarLayout) {
//            innerPlaybackToolbarLayout.removeAllViews();
//            innerPlaybackToolbarLayout.addView(skipToPreviousButton.getView());
//            innerPlaybackToolbarLayout.addView(playPauseButton.getView());
//            innerPlaybackToolbarLayout.addView(skipToNextButton.getView());
        }
    }



    @Override
    void initialiseDependencies() {
        FragmentActivity activity = getActivity();
        if (null != activity && activity instanceof MediaActivityCompat) {
            MediaActivityCompat mediaPlayerActivity = (MediaActivityCompat) getActivity();
            mediaPlayerActivity.getMediaActivityCompatComponent().playbackButtonsSubcomponent()
                    .inject(this);
        }
    }
}
