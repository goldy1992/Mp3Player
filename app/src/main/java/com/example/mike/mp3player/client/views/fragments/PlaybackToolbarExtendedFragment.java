package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.MediaPlayerActvityRequester;
import com.example.mike.mp3player.client.views.LinearLayoutWithImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PlaybackToolbarExtendedFragment extends PlayToolBarFragment {

    protected LinearLayoutWithImageView skipToPreviousButton;
    protected LinearLayoutWithImageView skipToNextButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LinearLayout root = new LinearLayout(getContext());
        return inflater.inflate(R.layout.fragment_playback_toolbar, root, true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.skipToPreviousButton = LinearLayoutWithImageView.create(getContext(), R.drawable.ic_baseline_skip_previous_24px, (View v) -> skipToPrevious());
        this.skipToNextButton = LinearLayoutWithImageView.create(getContext(), R.drawable.ic_baseline_skip_next_24px, (View v) -> skipToNext());
    }

    public void init(MediaControllerAdapter mediaControllerAdapter) {
        super.init(mediaControllerAdapter, true);
    }

    public void skipToNext() {
        mediaControllerAdapter.skipToNext();
    }

    public void skipToPrevious() {
        mediaControllerAdapter.skipToPrevious();
    }

    @Override
    public void displayButtons() {
        if (null != innerPlaybackToolbarLayout) {
            skipToPreviousButton.setLayoutParams(getLinearLayoutParams(skipToPreviousButton.getLayoutParams()));
            innerPlaybackToolbarLayout.addView(skipToPreviousButton);

            super.displayButtons();
            skipToNextButton.setLayoutParams(getLinearLayoutParams(skipToNextButton.getLayoutParams()));
            innerPlaybackToolbarLayout.addView(skipToNextButton);
        }
    }

    @Deprecated // no need to make empty views in the toolbar anymore
    private View getEmptyViewForToolbar(int widthOfDivision, int layoutHeight) {
        View view = new View(getContext());
        view.setLayoutParams(new LinearLayout.LayoutParams(widthOfDivision, layoutHeight));
        view.setBackgroundColor(getResources().getColor(android.R.color.holo_purple, null));
        view.setVisibility(View.INVISIBLE);
        return view;
    }


}
