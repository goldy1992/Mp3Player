package com.example.mike.mp3player.service.player;

import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;

public class MyTimelineQueueNavigator extends TimelineQueueNavigator {
    public MyTimelineQueueNavigator(MediaSessionCompat mediaSession) {
        super(mediaSession);
    }

    @Override
    public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder().setMediaId("145").build();
        return mediaDescriptionCompat;
    }
}
