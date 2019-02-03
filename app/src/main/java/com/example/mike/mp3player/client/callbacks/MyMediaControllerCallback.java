package com.example.mike.mp3player.client.callbacks;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.client.activities.MediaActivityCompat;
import com.example.mike.mp3player.client.MediaControllerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 04/10/2017.
 */

public class MyMediaControllerCallback extends MediaControllerCompat.Callback {

    private final MediaActivityCompat activity;
    private final MediaControllerAdapter mediaControllerAdapter;
    private List<MediaControllerCompat.Callback> childCallbacks;

    public MyMediaControllerCallback(MediaActivityCompat activity, MediaControllerAdapter mediaControllerAdapter) {
        this.activity = activity;
        this.mediaControllerAdapter = mediaControllerAdapter;
        this.childCallbacks = new ArrayList<>();
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        activity.setMetaData(metadata);
        if (!childCallbacks.isEmpty()) {
            for (MediaControllerCompat.Callback callback : childCallbacks) {
                callback.onMetadataChanged(metadata);
            }
        }
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        activity.setPlaybackState(state);
        if (!childCallbacks.isEmpty()) {
            for (MediaControllerCompat.Callback callback : childCallbacks) {
                callback.onPlaybackStateChanged(state);
            }
        }
    }

    public List<MediaControllerCompat.Callback> getChildCallbacks() {
        return childCallbacks;
    }
}
