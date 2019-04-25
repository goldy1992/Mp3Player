package com.example.mike.mp3player.client.callbacks.playback;

import android.support.v4.media.session.PlaybackStateCompat;

import org.jetbrains.annotations.NotNull;

public interface PlaybackStateListener {
    void onPlaybackStateChanged(@NotNull PlaybackStateCompat state);
}
