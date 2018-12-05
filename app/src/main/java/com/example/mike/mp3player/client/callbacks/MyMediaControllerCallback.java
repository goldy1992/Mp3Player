package com.example.mike.mp3player.client.callbacks;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.mike.mp3player.client.MediaActivityCompat;
import com.example.mike.mp3player.client.MediaControllerWrapper;
import com.example.mike.mp3player.client.MediaPlayerActivity;
import com.example.mike.mp3player.client.PlaybackStateWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mike on 04/10/2017.
 */

public class MyMediaControllerCallback< A extends MediaActivityCompat> extends MediaControllerCompat.Callback {

    private final A activity;
    private final MediaControllerWrapper mediaControllerWrapper;
    private List<MediaControllerCompat.Callback> childCallbacks;

    public MyMediaControllerCallback(A activity, MediaControllerWrapper mediaControllerWrapper) {
        this.activity = activity;
        this.mediaControllerWrapper = mediaControllerWrapper;
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
        PlaybackStateWrapper playbackStateWrapper = new PlaybackStateWrapper(state);
        mediaControllerWrapper.setCurrentPlaybackState(playbackStateWrapper);
        activity.setPlaybackState(playbackStateWrapper);
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
