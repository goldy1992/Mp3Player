package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.views.buttons.RepeatOneRepeatAllButton;
import com.example.mike.mp3player.client.views.buttons.ShuffleButton;
import com.example.mike.mp3player.dagger.components.MediaActivityCompatComponent;

import javax.inject.Inject;

public class MediaControlsFragment extends AsyncFragment {
    private static final String LOG_TAG = "PLY_PAUSE_BTN";
    private RepeatOneRepeatAllButton repeatOneRepeatAllButton;
    private ShuffleButton shuffleButton;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initialiseDependencies();
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_media_controls, container, true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.repeatOneRepeatAllButton.init(view.findViewById(R.id.repeatOneRepeatAllButton));
        this.shuffleButton.init(view.findViewById(R.id.shuffleButton));
    }


    public void initialiseDependencies() {
        MediaActivityCompatComponent component = ((MediaActivityCompat)getActivity())
                .getMediaActivityCompatComponent();
        component.playbackButtonsSubcomponent()
                .inject(this);
    }

    @Inject
    public void setRepeatOneRepeatAllButton(RepeatOneRepeatAllButton repeatOneRepeatAllButton) {
        this.repeatOneRepeatAllButton = repeatOneRepeatAllButton;
    }

    @Inject
    public void setShuffleButton(ShuffleButton shuffleButton) {
        this.shuffleButton = shuffleButton;
    }
}
