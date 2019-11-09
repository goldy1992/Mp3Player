package com.github.goldy1992.mp3player;

import android.content.Context;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.commons.MediaItemType;

import static com.github.goldy1992.mp3player.commons.Constants.MEDIA_ITEM_TYPE;
import static com.github.goldy1992.mp3player.commons.Constants.ROOT_ITEM_TYPE;
import static com.github.goldy1992.mp3player.commons.MediaItemType.ROOT;
import static com.github.goldy1992.mp3player.commons.MediaItemType.SONGS;

public final class TestUtils {
    private TestUtils(){}

    public static final MediaItem createMediaItem() {
        MediaDescriptionCompat mediaDescription = new MediaDescriptionCompat.Builder()
                .setMediaId("c").setTitle("title").build();
        return new MediaItem(mediaDescription, 0);
    }

    /**
     * @return a null category MediaItem
     */
    public static MediaItem createMediaItemWithNullCategory() {
        return new MediaItemBuilder("anId")
                .setRootItemType(null)
                .setMediaItemType(ROOT)
                .build();
    }
    /**
     * Utility class to make a media item
     * @param id the id
     * @param title the title
     * @param description the description
     * @return a new MediaItem
     */
    public static MediaItem createMediaItem(final String id, final String title, final String description, MediaItemType mediaItemType) {
        return createMediaItem(id, title, description, mediaItemType,0L);
    }

    public static MediaItem createMediaItem(final String id, final String title, final String description, MediaItemType mediaItemType, final long duration) {
        return createMediaItem(id, title, description, mediaItemType, duration, null);
    }

    public static MediaItem createMediaItem(final String id, final String title, final String description, MediaItemType mediaItemType, final long duration, String artist) {
        Bundle bundle = new Bundle();
        bundle.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration);
        bundle.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist);
        bundle.putSerializable(MEDIA_ITEM_TYPE, mediaItemType);
        bundle.putSerializable(ROOT_ITEM_TYPE, SONGS);
        MediaDescriptionCompat mediaDescriptionCompat = new MediaDescriptionCompat.Builder()
                .setDescription(description)
                .setTitle(title)
                .setMediaId(id)
                .setExtras(bundle)
                .build();
        return new MediaItem(mediaDescriptionCompat, 0);
    }

    public static MediaSessionCompat.Token getMediaSessionCompatToken(Context context) {
        MediaSession mediaSession = new MediaSession(context, "sd");
        MediaSession.Token sessionToken = mediaSession.getSessionToken();
        return MediaSessionCompat.Token.fromToken(sessionToken);
    }
}
