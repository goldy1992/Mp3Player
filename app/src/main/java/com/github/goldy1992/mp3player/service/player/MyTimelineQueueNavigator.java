package com.github.goldy1992.mp3player.service.player;

import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.github.goldy1992.mp3player.service.PlaylistManager;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;

public class MyTimelineQueueNavigator extends TimelineQueueNavigator {

    final PlaylistManager playlistManager;

    public MyTimelineQueueNavigator(MediaSessionCompat mediaSession, PlaylistManager playlistManager) {
        super(mediaSession);
        this.playlistManager = playlistManager;
    }

    @Override
    public MediaDescriptionCompat getMediaDescription(Player player, int windowIndex) {
        MediaItem mediaItem = playlistManager.getItemAtIndex(windowIndex);
        return mediaItem.getDescription();
    }
}
