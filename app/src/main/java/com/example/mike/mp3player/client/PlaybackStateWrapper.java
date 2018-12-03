package com.example.mike.mp3player.client;

import android.support.v4.media.session.PlaybackStateCompat;

public final class PlaybackStateWrapper {
    private final PlaybackStateCompat playbackState;
    private long timestanp;

    public PlaybackStateWrapper(PlaybackStateCompat playbackState) {
        this.playbackState = playbackState;
        this.timestanp = System.currentTimeMillis();
    }

    public PlaybackStateCompat getPlaybackState() {
        return playbackState;
    }

    public long getTimestanp() {
        return timestanp;
    }
}
