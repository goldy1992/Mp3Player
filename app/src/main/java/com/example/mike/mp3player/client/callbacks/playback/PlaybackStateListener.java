package com.example.mike.mp3player.client.callbacks.playback;

import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;

public interface PlaybackStateListener {
    void onPlaybackStateChanged(@NonNull PlaybackStateCompat state);
}
