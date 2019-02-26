package com.example.mike.mp3player.client.callbacks;

import android.os.Looper;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.example.mike.mp3player.client.callbacks.playback.PlaybackStateListener;
import com.example.mike.mp3player.client.callbacks.playback.MyPlaybackStateCallback;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mike on 04/10/2017.
 */
// TODO: ORGANIZE LISTENERS INTO CATEGORIES DEFINED BY THE ACTION THAT SHOULD BE SET IN THE ACTIONS LIST
public class MyMediaControllerCallback extends MediaControllerCompat.Callback {

    private static final String LOG_TAG = "MY_MDIA_CNTLR_CLLBCK";
    private final MyPlaybackStateCallback myPlaybackStateCallback;
    private final MyMetaDataCallback myMetaDataCallback;

    private Set<PlaybackStateListener> playbackStateListeners;

    public MyMediaControllerCallback(Looper looper) {
        this.myMetaDataCallback = new MyMetaDataCallback(looper);
        this.myPlaybackStateCallback = new MyPlaybackStateCallback(looper);
        this.playbackStateListeners = new HashSet<>();
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        this.getMyMetaDataCallback().onStateChanged(metadata);
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        this.getMyPlaybackStateCallback().onStateChanged(state);
    }

    public MyPlaybackStateCallback getMyPlaybackStateCallback() {
        return myPlaybackStateCallback;
    }

    public MyMetaDataCallback getMyMetaDataCallback() {
        return myMetaDataCallback;
    }
}
