package com.example.mike.mp3player.commons;

import static android.media.MediaMetadata.METADATA_KEY_ALBUM_ARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_ARTIST;
import static android.media.MediaMetadataRetriever.METADATA_KEY_DURATION;

public final class MetaDataKeys {
    public static final String META_DATA_KEY_PARENT_PATH = "META_DATA_KEY_PARENT_PATH";
    public static final String META_DATA_KEY_FILE_NAME = "META_DATA_KEY_FILE_NAME";
    public static final String STRING_METADATA_KEY_ARTIST = String.valueOf(METADATA_KEY_ARTIST);
    public static final String STRING_METADATA_KEY_DURATION = String.valueOf(METADATA_KEY_DURATION);
    public static final String STRING_METADATA_KEY_ALBUM_ARTIST = String.valueOf(METADATA_KEY_ALBUM_ARTIST);
}
