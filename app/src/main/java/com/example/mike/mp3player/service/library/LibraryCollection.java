package com.example.mike.mp3player.service.library;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;

import com.example.mike.mp3player.client.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_PARENT_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

@Dao
public abstract class LibraryCollection {


    public abstract String[] getProjection();
    public abstract TreeSet<MediaItem> getChildren(LibraryObject id);
    public abstract TreeSet<MediaItem> getAllChildren();
    public abstract List<MediaItem> search(String query);
    public abstract Category getRootId();
    protected ContentResolver contentResolver;

    @SuppressWarnings("unchecked")
    public LibraryCollection(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public TreeSet<MediaItem> convertMediaItems(List<MediaItem> roots, Comparator<MediaItem> comparator) {
        TreeSet<MediaItem> mediaItemTreeSet = new TreeSet<>(comparator);
        for (MediaItem root : roots) {
            mediaItemTreeSet.add(root);
        }
        return mediaItemTreeSet;
    }

    public Cursor query(@NonNull final String[] projection,
                                         @Nullable final String selection,
                                         @Nullable final String[] selectionArgs,
                                         @NonNull Comparator<MediaItem> comparator) {
        TreeSet<MediaItem> listToReturn = new TreeSet<>(comparator);
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection,
                selection, selectionArgs, null);

        while (cursor.moveToNext()) {
            listToReturn.add(createSongMediaItem(cursor));
        }

        return null;

    }

    private MediaBrowserCompat.MediaItem createSongMediaItem(Cursor c){
        String path = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
        long duration = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION));
        String artist = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        String title = c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE));
        String album = c.getString(c.getColumnIndex(MediaStore.Audio.Media.ALBUM));
        long albumId = c.getLong(c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
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
        extras.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration);
        extras.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist);
        extras.putString(META_DATA_KEY_PARENT_PATH, parentPath);
        extras.putString(META_DATA_KEY_FILE_NAME, fileName);
        extras.putString(META_DATA_PARENT_DIRECTORY_NAME, directoryName);
        extras.putString(META_DATA_PARENT_DIRECTORY_PATH, directoryPath);
        extras.putParcelable(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, albumArtUri);

        // TODO: add code to fetch album art also
        MediaDescriptionCompat.Builder mediaDescriptionCompatBuilder = new MediaDescriptionCompat.Builder()
                .setMediaId(mediaId)
                .setMediaUri(uri)
                .setTitle(title)
                .setExtras(extras);

        return new MediaBrowserCompat.MediaItem(mediaDescriptionCompatBuilder.build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
    }
}
