package com.example.mike.mp3player.service.player;

import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.mike.mp3player.service.PlaybackManager;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;

public class MyTimelineQueueNavigator extends TimelineQueueNavigator {

    final PlaybackManager playbackManager;

    public MyTimelineQueueNavigator(MediaSessionCompat mediaSession, PlaybackManager playbackManager) {
        super(mediaSession);
        this.playbackManager = playbackManager;
    }

    @Override
    public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
        MediaItem mediaItem = playbackManager.getItemAtIndex(windowIndex);
        return mediaItem.getDescription();
    }
}
