package com.example.mike.mp3player.service.player;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.service.PlaybackManager;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;

import static com.example.mike.mp3player.commons.Constants.UNKNOWN;
import static com.example.mike.mp3player.commons.MediaItemUtils.getAlbumArtPath;
import static com.example.mike.mp3player.commons.MediaItemUtils.getArtist;
import static com.example.mike.mp3player.commons.MediaItemUtils.getMediaId;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;

public class MyMetadataProvider implements MediaSessionConnector.MediaMetadataProvider {

    private final PlaybackManager playbackManager;

    public MyMetadataProvider(PlaybackManager playbackManager) {
        this.playbackManager = playbackManager;
    }

    @Override
    public MediaMetadataCompat getMetadata(Player player) {

        final int currentIndex = player.getCurrentWindowIndex();
        MediaBrowserCompat.MediaItem currentItem = playbackManager.getItemAtIndex(currentIndex);

        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        builder.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, MediaItemUtils.getDuration(currentItem));

        String mediaId = getMediaId(currentItem);
        builder.putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId);

        String title = getTitle(currentItem);
        builder.putString(MediaMetadataCompat.METADATA_KEY_TITLE, null != title ? title : UNKNOWN);

        String artist = getArtist(currentItem);
        builder.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, null != artist ? artist : UNKNOWN);

        String albumArt = getAlbumArtPath(currentItem);
        builder.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, albumArt);
        return builder.build();
    }
}
