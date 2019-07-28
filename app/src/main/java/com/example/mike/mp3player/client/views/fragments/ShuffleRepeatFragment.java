package com.example.mike.mp3player.client.views.fragments;

import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.mike.mp3player.R;
import com.example.mike.mp3player.client.MediaControllerAdapter;
import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.callbacks.playback.ListenerType;
import com.example.mike.mp3player.client.views.RepeatOneRepeatAllButton;
import com.example.mike.mp3player.client.views.ShuffleButton;
import com.example.mike.mp3player.dagger.components.MediaActivityCompatComponent;

import java.util.Collections;

import javax.inject.Inject;

public class ShuffleRepeatFragment extends AsyncFragment {
    private static final String LOG_TAG = "PLY_PAUSE_BTN";
    private MediaControllerAdapter mediaControllerAdapter;
    private RepeatOneRepeatAllButton repeatOneRepeatAllButton;
    private ShuffleButton shuffleButton;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initialiseDependencies();
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_shuffle_repeat, container, true);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.repeatOneRepeatAllButton = view.findViewById(R.id.repeatOneRepeatAllButton);
        this.shuffleButton = view.findViewById(R.id.shuffleButton);

        this.mediaControllerAdapter.registerPlaybackStateListener(repeatOneRepeatAllButton, Collections.singleton(ListenerType.REPEAT));
        this.mediaControllerAdapter.registerPlaybackStateListener(shuffleButton, Collections.singleton(ListenerType.SHUFFLE));
    }





    @Inject
    public void setMediaControllerAdapter(MediaControllerAdapter mediaControllerAdapter) {
        this.mediaControllerAdapter = mediaControllerAdapter;
    }

    public void initialiseDependencies() {
        MediaActivityCompatComponent component = ((MediaActivityCompat)getActivity())
                .getMediaActivityCompatComponent();
        component.inject(this);
    }
}
