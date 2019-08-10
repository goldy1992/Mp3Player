package com.example.mike.mp3player.service.library.contentretriever;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST;
import static android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DURATION;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_PARENT_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

public class SongsRetriever extends ContentResolverRetriever {

    private static final String[] PROJECTION = {
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM_ID};

    public SongsRetriever(ContentResolver contentResolver, String typeId) {
        super(contentResolver, typeId);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.SONG;
    }

    @Override
    public String[] getProjection() {
        return PROJECTION;
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> getChildren(@NonNull String id) {
        List<MediaBrowserCompat.MediaItem> listToReturn = new ArrayList<>();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,PROJECTION,
                null, null, null);
        while (cursor.moveToNext()) {
            listToReturn.add(createPlayableMediaItemFromCursor(cursor));
        }
        return listToReturn;
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> search(@NonNull String query) {
        return null;
    }

    private MediaBrowserCompat.MediaItem createPlayableMediaItemFromCursor(Cursor c){
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

        Bundle extras = new Bundle();
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

        return new MediaBrowserCompat.MediaItem(mediaDescriptionCompatBuilder.build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
    }
}
