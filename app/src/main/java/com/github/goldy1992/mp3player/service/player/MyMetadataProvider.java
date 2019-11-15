package com.github.goldy1992.mp3player.service.player;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.github.goldy1992.mp3player.commons.MediaItemUtils;
import com.github.goldy1992.mp3player.service.PlaylistManager;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;

import static com.github.goldy1992.mp3player.commons.Constants.UNKNOWN;
import static com.github.goldy1992.mp3player.commons.MediaItemUtils.getAlbumArtPath;
import static com.github.goldy1992.mp3player.commons.MediaItemUtils.getArtist;
import static com.github.goldy1992.mp3player.commons.MediaItemUtils.getMediaId;
import static com.github.goldy1992.mp3player.commons.MediaItemUtils.getTitle;

public class MyMetadataProvider implements MediaSessionConnector.MediaMetadataProvider {

    private final PlaylistManager playlistManager;

    public MyMetadataProvider(PlaylistManager playlistManager) {
        this.playlistManager = playlistManager;
    }

    @Override
    public MediaMetadataCompat getMetadata(Player player) {

        final int currentIndex = player.getCurrentWindowIndex();
        MediaBrowserCompat.MediaItem currentItem = playlistManager.getItemAtIndex(currentIndex);

        if (null == currentItem) {
            return null;
        }
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
