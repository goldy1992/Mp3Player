package com.github.goldy1992.mp3player.client.callbacks;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.github.goldy1992.mp3player.client.callbacks.metadata.MyMetadataCallback;
import com.github.goldy1992.mp3player.client.callbacks.playback.MyPlaybackStateCallback;

import javax.inject.Inject;

/**
 * Created by Mike on 04/10/2017.
 */
// TODO: ORGANIZE LISTENERS INTO CATEGORIES DEFINED BY THE ACTION THAT SHOULD BE SET IN THE ACTIONS LIST
public class MyMediaControllerCallback extends MediaControllerCompat.Callback {

    private static final String LOG_TAG = "MY_MDIA_CNTLR_CLLBCK";
    private final MyPlaybackStateCallback myPlaybackStateCallback;
    private final MyMetadataCallback myMetaDataCallback;

    @Inject
    public MyMediaControllerCallback(MyMetadataCallback myMetaDataCallback,
                                     MyPlaybackStateCallback myPlaybackStateCallback) {
        this.myMetaDataCallback = myMetaDataCallback;
        this.myPlaybackStateCallback = myPlaybackStateCallback;
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        this.myMetaDataCallback.onStateChanged(metadata);
    }

    @Override
    public void onPlaybackStateChanged(PlaybackStateCompat state) {
        this.myPlaybackStateCallback.onStateChanged(state);
    }

    public MyPlaybackStateCallback getMyPlaybackStateCallback() {
        return myPlaybackStateCallback;
    }

    public MyMetadataCallback getMyMetaDataCallback() {
        return myMetaDataCallback;
    }
}
