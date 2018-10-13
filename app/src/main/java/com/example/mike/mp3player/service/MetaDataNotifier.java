package com.example.mike.mp3player.service;

import android.media.MediaPlayer;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

public class MetaDataNotifier {

    private MediaSessionCompat mediaSession;
    private MediaMetadataCompat.Builder mediaMetadataCompatBuilder;

    public MetaDataNotifier(MediaSessionCompat mediaSession) {
        this.mediaSession = mediaSession;

    }

    public void notifyMetaDataChange(MediaPlayer mediaPlayer) {
        mediaMetadataCompatBuilder = new MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaPlayer.getDuration());
        mediaSession.setMetadata(mediaMetadataCompatBuilder.build());
    }
}
