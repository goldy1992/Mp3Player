package com.example.mike.mp3player.commons;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mike.mp3player.client.utils.TimerUtils;

import org.apache.commons.io.FilenameUtils;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION;
import static com.example.mike.mp3player.commons.Constants.LIBRARY_ID;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE_ID;
import static com.example.mike.mp3player.commons.Constants.ROOT_ITEM_TYPE;
import static com.example.mike.mp3player.commons.Constants.UNKNOWN;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;

public final class MediaItemUtils {

    public static boolean hasExtras(MediaItem item) {
        return item != null && item.getDescription().getExtras() != null;
    }


    public static boolean hasTitle(MediaItem item) {
        return item != null && item.getDescription().getTitle() != null;
    }

    public static boolean hasDescription(MediaItem item) {
        return item != null && item.getDescription().getDescription() != null;
    }

    public static Bundle getExtras(MediaItem item) {
        if (!hasExtras(item)) {
            return null;
        }
        return item.getDescription().getExtras();
    }

    public static Object getExtra(String key, MediaItem item) {
        final Bundle extras = item.getDescription().getExtras();
        return null == extras ? null : extras.get(key);
    }

    public static String getMediaId(MediaItem item) {
        if (null != item) {
            return item.getDescription().getMediaId();
        }
        return null;
    }

    public static String getTitle(MediaItem i) {
        if (hasTitle(i)) {
            return i.getDescription().getTitle().toString();
        }
        return null;
    }

    public static String getDescription(MediaItem item) {
        if (hasDescription(item)) {
            return item.getDescription().getDescription().toString();
        }
        return null;
    }

    public static boolean hasExtra(String key, MediaItem item) {
        return hasExtras(item) && item.getDescription().getExtras().containsKey(key);
    }

    public static String getArtist(MediaItem item) {
         return (String) getExtra(METADATA_KEY_ARTIST, item);
    }

    public static String getAlbumArtPath(MediaItem item) {
        if (hasExtra(METADATA_KEY_ALBUM_ART_URI, item)) {
            Uri uri = (Uri) getExtra(METADATA_KEY_ALBUM_ART_URI, item);
            if (uri != null) {
                return uri.toString();
            }
        }
        return null;
    }

    public static String getDirectoryName(MediaItem item) {
        if (hasExtra(META_DATA_PARENT_DIRECTORY_NAME, item)) {
            return  (String) getExtra(META_DATA_PARENT_DIRECTORY_NAME, item);
        }
        return null;
    }

    public static Uri getMediaUri(MediaItem item) {
        return item.getDescription().getMediaUri();
    }

    public static long getDuration(MediaItem item) {
        return (long) getExtra(METADATA_KEY_DURATION, item);
    }

    public static MediaItemType getMediaItemType(MediaItem item) {
        return (MediaItemType)getExtra(MEDIA_ITEM_TYPE, item);
    }

    public static String getLibraryId(MediaItem item) {
        return (String) getExtra(LIBRARY_ID, item);
    }

    public static MediaItemType getRootMediaItemType(MediaItem item) {
        return (MediaItemType)getExtra(ROOT_ITEM_TYPE, item);
    }

    public static String extractFolderName(MediaItem song) {
        return getTitle(song);
    }

    public static String extractFolderPath(MediaItem song) {
        return getDescription(song);
    }

    public static String extractTitle(MediaItem song) {
        CharSequence charSequence = song.getDescription().getTitle();
        if (null == charSequence) {
            String fileName = hasExtras(song) ? (String) getExtra(MetaDataKeys.META_DATA_KEY_FILE_NAME, song) : null;
            if (fileName != null) {
                return FilenameUtils.removeExtension(fileName);
            }
        } else {
            return charSequence.toString();
        }
        return UNKNOWN;
    }

    public static String extractArtist(MediaItem song) {
        String artist = null;
        try {
            artist = song.getDescription().getExtras().getString(METADATA_KEY_ARTIST);
            if (null == artist) {
                artist = UNKNOWN;
            }
        } catch (NullPointerException ex) {
            artist = UNKNOWN;
        }
        return artist;
    }

    public static String extractDuration(@NonNull MediaItem song) {
        Bundle extras =  song.getDescription().getExtras();
        if (null != extras) {
            long duration = extras.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
            return TimerUtils.formatTime(duration);
        }
        return null;
    }

    public static Uri getAlbumArtUri(@NonNull MediaItem song) {
        Bundle extras = song.getDescription().getExtras();
        if (null != extras) {
            return  (Uri) extras.get(METADATA_KEY_ALBUM_ART_URI);
        }
        return null;
    }

    @Nullable
    public static String getRootTitle(@NonNull MediaItem song) {
        Bundle extras = song.getDescription().getExtras();
        if (null != extras) {
            MediaItemType mediaItemType = (MediaItemType) extras.getSerializable(ROOT_ITEM_TYPE);
            return mediaItemType == null ? null : mediaItemType.getTitle();
        }
        return null;
    }
}
