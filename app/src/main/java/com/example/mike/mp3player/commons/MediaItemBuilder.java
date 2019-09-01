package com.example.mike.mp3player.commons;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;
import static com.example.mike.mp3player.commons.Constants.ROOT_ITEM_TYPE;

public class MediaItemBuilder {

    private final String mediaId;
    private String description = null;
    private String title = null;
    private Bundle extras;


    public MediaItemBuilder(String mediaId) {
        this.mediaId = mediaId;
        this.extras = new Bundle();
    }

    public MediaItemBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public MediaItemBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public MediaItemBuilder setRootItemType(MediaItemType rootItemType) {
        this.extras.putSerializable(ROOT_ITEM_TYPE, rootItemType);
        return this;
    }

    public MediaItemBuilder setMediaItemType(MediaItemType mediaItemType) {
        this.extras.putSerializable(MEDIA_ITEM_TYPE, mediaItemType);
        return this;
    }

    public MediaItemBuilder setDuration(long duration) {
        this.extras.putLong(METADATA_KEY_DURATION, duration);
        return this;
    }

    public MediaItemBuilder setArtist(String artist) {
        this.extras.putString(METADATA_KEY_ARTIST, artist);
        return this;
    }

    public MediaBrowserCompat.MediaItem build() {
        MediaDescriptionCompat mediaDescription =
                new MediaDescriptionCompat.Builder()
                    .setMediaId(mediaId)
                    .setTitle(title)
                    .setDescription(description)
                    .setExtras(extras)
                    .build();

        return new MediaBrowserCompat.MediaItem(mediaDescription, 0);
    }
}
