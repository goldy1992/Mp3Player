package com.example.mike.mp3player.service.library.content.parser;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.MediaItemType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.annotation.Nullable;

import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION;
import static com.example.mike.mp3player.commons.ComparatorUtils.uppercaseStringCompare;
import static com.example.mike.mp3player.commons.Constants.LIBRARY_ID;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_PARENT_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

public class SongResultsParser extends ResultsParser {

    @Override
    public List<MediaItem> create(Cursor cursor, String libraryIdPrefix) {
        TreeSet<MediaItem> listToReturn = new TreeSet<>(this);
        while (cursor!= null && cursor.moveToNext()) {
            MediaItem mediaItem = buildMediaItem(cursor, libraryIdPrefix);
            listToReturn.add(mediaItem);
        }
        return new ArrayList<>(listToReturn);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.SONG;
    }

    private MediaItem buildMediaItem(Cursor c, String libraryIdPrefix) {
        final String mediaId = c.getString(c.getColumnIndex(MediaStore.Audio.Media._ID));
        final String mediaFilePath = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
        final long duration = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
        final String artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        final String title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
        final long albumId = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
        final File mediaFile =  new File(mediaFilePath);
        final File mediaDirectory = mediaFile.getParentFile();
        final Uri mediaUri = Uri.fromFile(mediaFile);

        String directoryName = null;
        String  directoryPath = null;

        if (null != mediaDirectory) {
            directoryName = mediaDirectory.getName();
            directoryPath = mediaDirectory.getAbsolutePath();
        }

        String parentPath = null;

        if (null != mediaFile.getParent()) {
            parentPath = mediaFile.getParentFile().getAbsolutePath();
        }
        String fileName = mediaFile.getName();

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

        Bundle extras = getExtras();
        extras.putString(LIBRARY_ID, buildLibraryId(libraryIdPrefix, mediaId));
        extras.putLong(METADATA_KEY_DURATION, duration);
        extras.putString(METADATA_KEY_ARTIST, artist);
        extras.putString(META_DATA_KEY_PARENT_PATH, parentPath);
        extras.putString(META_DATA_KEY_FILE_NAME, fileName);
        extras.putString(META_DATA_PARENT_DIRECTORY_NAME, directoryName);
        extras.putString(META_DATA_PARENT_DIRECTORY_PATH, directoryPath);
        extras.putParcelable(METADATA_KEY_ALBUM_ART_URI, albumArtUri);

        // TODO: add code to fetch album art also
        MediaDescriptionCompat.Builder mediaDescriptionCompatBuilder = new MediaDescriptionCompat.Builder()
                .setMediaId(mediaId)
                .setMediaUri(mediaUri)
                .setTitle(title)
                .setExtras(extras);

        return new MediaItem(mediaDescriptionCompatBuilder.build(), MediaItem.FLAG_PLAYABLE);
    }

    @Override
    public int compare(MediaItem m1, MediaItem m2) {
        return uppercaseStringCompare(getTitle(m1), getTitle(m2));
    }

    private String buildLibraryId(@Nullable String mediaIdPrefix, String mediaIdSuffix) {
        if (mediaIdPrefix == null) {
            return mediaIdSuffix;
        } else {
            return mediaIdPrefix + "|" + mediaIdSuffix;
        }
    }
}
