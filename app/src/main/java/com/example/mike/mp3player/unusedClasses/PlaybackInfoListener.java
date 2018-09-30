package com.example.mike.mp3player.unusedClasses;

/**
 * Created by michael.goldsmith on 18/10/2017.
 */
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.service.MediaPlaybackService;

/**
 * Listener to provide state updates from {@link MediaPlayerAdapter} (the media player)
 * to {@link MediaPlaybackService} (the service that holds our {@link MediaSessionCompat}.
 */
public abstract class PlaybackInfoListener {

    public abstract void onPlaybackStateChange(PlaybackStateCompat state);

    public void onPlaybackCompleted() {
    }
}