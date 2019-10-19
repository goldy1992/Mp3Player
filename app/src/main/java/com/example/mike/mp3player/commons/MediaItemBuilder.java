package com.example.mike.mp3player.commons;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import java.io.File;

import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION;
import static com.example.mike.mp3player.commons.Constants.LIBRARY_ID;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;
import static com.example.mike.mp3player.commons.Constants.ROOT_ITEM_TYPE;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_DIRECTORY;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.service.library.content.parser.SongResultsParser.ALBUM_ART_URI_PREFIX;

public class MediaItemBuilder {

    private final String mediaId;
    private String description = null;
    private String title = null;
    private Uri mediaUri = null;
    private Bundle extras;
    private int flags;

    public MediaItemBuilder(String mediaId) {
        this.mediaId = mediaId;
        this.extras = new Bundle();
    }

    public MediaItemBuilder setFlags(int flags) {
        this.flags = flags;
        return this;
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

    public MediaItemBuilder setLibraryId(String libraryId) {
        this.extras.putString(LIBRARY_ID, libraryId);
        return this;
    }

    public MediaItemBuilder setFileName(String fileName) {
        this.extras.putString(META_DATA_KEY_FILE_NAME, fileName);
        return this;
    }

    public MediaItemBuilder setAlbumArtUri(Uri albumArtUri) {
        this.extras.putParcelable(METADATA_KEY_ALBUM_ART_URI, albumArtUri);
        return this;
    }

    public MediaItemBuilder setMediaUri(Uri mediaUri) {
        this.mediaUri = mediaUri;
        return this;
    }

    public MediaItemBuilder setFile(File file) {
        extras.putSerializable(META_DATA_DIRECTORY, file);
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
                    .setMediaUri(mediaUri)
                    .setTitle(title)
                    .setDescription(description)
                    .setExtras(extras)
                    .build();

        return new MediaBrowserCompat.MediaItem(mediaDescription, flags);
    }
}
