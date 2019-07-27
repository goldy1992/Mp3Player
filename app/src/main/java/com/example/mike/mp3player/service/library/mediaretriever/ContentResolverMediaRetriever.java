package com.example.mike.mp3player.service.library.mediaretriever;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import androidx.annotation.VisibleForTesting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_ALBUM_ART_URI;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_PARENT_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_ARTIST;
import static com.example.mike.mp3player.commons.MetaDataKeys.STRING_METADATA_KEY_DURATION;

public class ContentResolverMediaRetriever extends MediaRetriever {
    private ContentResolver contentResolver;

    @Inject
    public ContentResolverMediaRetriever(Context context) {
        super(context);
        this.contentResolver = context.getContentResolver();
    }

    private static final String[] PROJECTION = {
            Media.DATA,
            Media.DURATION,
            Media.ARTIST,
            Media.TITLE,
            Media.ALBUM,
            Media.ALBUM_ID};
    @Override
    public List<MediaBrowserCompat.MediaItem> retrieveMedia() {
        List<MediaBrowserCompat.MediaItem> listToReturn = new ArrayList<>();
        Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI ,PROJECTION,
                null, null, null);
        while (cursor.moveToNext()) {
            listToReturn.add(createPlayableMediaItemFromCursor(cursor));
        }
        return listToReturn;
    }

    private MediaBrowserCompat.MediaItem createPlayableMediaItemFromCursor(Cursor c){
        String path = c.getString(c.getColumnIndex(Media.DATA));
        long duration = c.getLong(c.getColumnIndex(Media.DURATION));
        String artist = c.getString(c.getColumnIndex(Media.ARTIST));
        String title = c.getString(c.getColumnIndex(Media.TITLE));
        String album = c.getString(c.getColumnIndex(Media.ALBUM));
        long albumId = c.getLong(c.getColumnIndex(Media.ALBUM_ID));
        File file =  new File(path);
        File directory = file.getParentFile();

        String directoryName = null;
        String  directoryPath = null;

        if (null != directory) {
            directoryName = directory.getName();
            directoryPath = directory.getAbsolutePath();
        }

        Uri uri = Uri.fromFile(file);
        String mediaId = String.valueOf(uri.getPath().hashCode());
        String parentPath = null;

        if (null != file.getParent()) {
            parentPath = file.getParentFile().getAbsolutePath();
        }
        String fileName = file.getName();

        Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

        Bundle extras = new Bundle();
        extras.putString(STRING_METADATA_KEY_DURATION, String.valueOf(duration));
        extras.putString(STRING_METADATA_KEY_ARTIST, artist);
        extras.putString(META_DATA_KEY_PARENT_PATH, parentPath);
        extras.putString(META_DATA_KEY_FILE_NAME, fileName);
        extras.putString(META_DATA_PARENT_DIRECTORY_NAME, directoryName);
        extras.putString(META_DATA_PARENT_DIRECTORY_PATH, directoryPath);
        extras.putParcelable(META_DATA_ALBUM_ART_URI, albumArtUri);

        // TODO: add code to fetch album art also
        MediaDescriptionCompat.Builder mediaDescriptionCompatBuilder = new MediaDescriptionCompat.Builder()
                .setMediaId(mediaId)
                .setMediaUri(uri)
                .setTitle(title)
                .setExtras(extras);

        return new MediaBrowserCompat.MediaItem(mediaDescriptionCompatBuilder.build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
    }

    @VisibleForTesting
    public ContentResolver getContentResolver() {
        return this.contentResolver;
    }

    @VisibleForTesting
    public static String[] getProjection() {
        return PROJECTION;
    }
}
