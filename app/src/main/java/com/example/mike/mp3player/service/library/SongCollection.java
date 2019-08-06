package com.example.mike.mp3player.service.library;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.example.mike.mp3player.commons.ComparatorUtils;
import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.service.library.db.AppDatabase;
import com.example.mike.mp3player.service.library.db.Song;
import com.example.mike.mp3player.service.library.db.SongDao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_PARENT_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

public class SongCollection extends LibraryCollection<Song> {

    private static final String[] PROJECTION = {
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID};

    public static final String ID = Category.SONGS.name();
    public static final String TITLE = Constants.CATEGORY_SONGS_TITLE;
    public static final String DESCRIPTION = Constants.CATEGORY_SONGS_DESCRIPTION;

    public SongCollection(SongDao dao, Context context) {
        super(ID, TITLE, DESCRIPTION, ComparatorUtils.compareMediaItemsByTitle, null, dao, context);
    }

    @Override
    public TreeSet<MediaBrowserCompat.MediaItem> getChildren(LibraryObject id) {
        ContentResolver contentResolver = context.getContentResolver();
        TreeSet<MediaBrowserCompat.MediaItem> listToReturn = new TreeSet<>(ComparatorUtils.compareMediaItemsByTitle);
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,PROJECTION,
                null, null, null);
        while (cursor.moveToNext()) {
            listToReturn.add(createPlayableMediaItemFromCursor(cursor));
        }
        return listToReturn;
    }

    @Override
    public TreeSet<MediaBrowserCompat.MediaItem> getAllChildren() {

        ContentResolver contentResolver = context.getContentResolver();
        TreeSet<MediaBrowserCompat.MediaItem> listToReturn = new TreeSet<>(ComparatorUtils.compareMediaItemsByTitle);
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,PROJECTION,
                null, null, null);
        while (cursor.moveToNext()) {
            listToReturn.add(createPlayableMediaItemFromCursor(cursor));
        }
        return listToReturn;

    }

    @Override
    public void index(List<MediaBrowserCompat.MediaItem> items) {
        AppDatabase database = null;
        List<Song> songs = new ArrayList<>();
        for (MediaBrowserCompat.MediaItem mediaItem : items) {
            Song song = new Song();

       //     song.mediaItem = mediaItem;
            songs.add(song);
        }
        Song[] mediaItemsArray = new Song[items.size()];
        mediaItemsArray = songs.toArray(mediaItemsArray);
        database.songDao().insertAll(mediaItemsArray);
        if (items != null) {
            this.getKeys().addAll(items);
        }
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> search(String query) {
        return null;
    }

    @Override
    public Category getRootId() {
        return Category.SONGS;
    }

    @Override
    public MediaBrowserCompat.MediaItem build(Song song) {
        Bundle extras = new Bundle();
        extras.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.artist);
        extras.putLong(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, song.albumId);
        extras.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, song.duration);
        MediaDescriptionCompat description = new MediaDescriptionCompat.Builder()
                .setMediaId(song.uri)
                .setTitle(song.title)
                .setExtras(extras)
                .build();
        return new MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE);
    }

    @Override
    public TreeSet<MediaBrowserCompat.MediaItem> convert(List<Song> list) {
        return super.convert(list, ComparatorUtils.compareMediaItemsByTitle);
    }

    public TreeSet<MediaBrowserCompat.MediaItem> getSongs() {
        return getKeys();
    }




    private MediaBrowserCompat.MediaItem createPlayableMediaItemFromCursor(Cursor c){
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
