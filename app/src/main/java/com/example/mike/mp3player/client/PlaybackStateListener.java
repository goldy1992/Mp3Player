package com.example.mike.mp3player.client;

import android.support.v4.media.session.PlaybackStateCompat;

public interface PlaybackStateListener {
    void onPlaybackStateChanged(PlaybackStateCompat state);
}
