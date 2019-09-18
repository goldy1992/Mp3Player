package com.example.mike.mp3player.service;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.Nullable;

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;

public class MyDescriptionAdapter implements PlayerNotificationManager.MediaDescriptionAdapter {
    private final PlaybackManager playbackManager;

    public MyDescriptionAdapter(PlaybackManager playbackManager) {
        this.playbackManager = playbackManager;
    }

    @Override
    public String getCurrentContentTitle(Player player) {
        return MediaItemUtils.getTitle(getCurrentMediaItem(player));
    }

    @Nullable
    @Override
    public PendingIntent createCurrentContentIntent(Player player) {
        return null;
    }

    @Nullable
    @Override
    public String getCurrentContentText(Player player) {
        return null;
    }

    @Nullable
    @Override
    public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
        return null;
    }

    private final MediaItem getCurrentMediaItem(Player player) {
        int position = player.getCurrentWindowIndex();
        return playbackManager.getItemAtIndex(position);
    }
}
