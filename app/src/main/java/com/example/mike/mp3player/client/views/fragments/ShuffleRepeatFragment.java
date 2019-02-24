package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.views.RepeatOneRepeatAllButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ShuffleRepeatFragment extends Fragment {
    private static final String LOG_TAG = "PLY_PAUSE_BTN";
    MediaControllerAdapter mediaControllerAdapter;
    RepeatOneRepeatAllButton repeatOneRepeatAllButton;
    boolean attachToRoot;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_shuffle_repeat, container, attachToRoot );
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        repeatOneRepeatAllButton = view.findViewById(R.id.repeatOneRepeatAllButton);
        repeatOneRepeatAllButton.setRepeatAllIcon();
        repeatOneRepeatAllButton.setOnClickListener((View v) -> setRepeatOneRepeatAllButtonMode(v));
    }

    public void init(MediaControllerAdapter mediaControllerAdapter, boolean attachToRoot) {
        this.mediaControllerAdapter = mediaControllerAdapter;
        if (this.repeatOneRepeatAllButton == null) {
            this.repeatOneRepeatAllButton = RepeatOneRepeatAllButton.create(mediaControllerAdapter.getContext());
        }
        this.mediaControllerAdapter.registerPlaybackStateListener(repeatOneRepeatAllButton);
        this.attachToRoot = attachToRoot;
    }

    void setRepeatOneRepeatAllButtonMode(View view) {

        int nextState = repeatOneRepeatAllButton.getNextState();
        // TODO: activate statement when MediaPlaybackService is ready to consume repeat requests
       // mediaControllerAdapter.setRepeatMode(nextState);
        repeatOneRepeatAllButton.updateState(repeatOneRepeatAllButton.getNextState());
    }


    public void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    public static ShuffleRepeatFragment createAndInitialiseShuffleRepeatFragment(MediaControllerAdapter mediaControllerAdapter, boolean attachToRoot) {
        ShuffleRepeatFragment shuffleRepeatFragment = new ShuffleRepeatFragment();
        shuffleRepeatFragment.init(mediaControllerAdapter, attachToRoot);
        return shuffleRepeatFragment;
    }


}
