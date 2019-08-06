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
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.service.library.db.Folder;
import com.example.mike.mp3player.service.library.db.FolderDao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.ComparatorUtils.compareMediaItemsByTitle;
import static com.example.mike.mp3player.commons.MediaItemUtils.getExtras;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_FILE_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_KEY_PARENT_PATH;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

public class FolderLibraryCollection extends LibraryCollection<Folder> {

    private static final String[] PROJECTION = {
            "DISTINCT " + MediaStore.Audio.Media.DATA };

    private final String LOG_TAG = "FOLDER_LIB_COLLECTION";
    public static final String ID = Constants.CATEGORY_FOLDERS_ID;
    public static final String TITLE = Constants.CATEGORY_FOLDERS_TITLE;
    public static final String DESCRIPTION = Constants.CATEGORY_FOLDERS_DESCRIPTION;

    public FolderLibraryCollection(FolderDao dao, Context context) {
        super(ID, TITLE, DESCRIPTION, compareMediaItemsByTitle, compareMediaItemsByTitle, dao, context);
        this.collection = new TreeMap<>();
    }

    @Override
    public void index(List<MediaItem> items) {
        if (null != items) {
            for (MediaItem i : items) {
                String folderPath = null;
                String folderName = null;

                if (MediaItemUtils.hasExtras(i)) {
                    folderPath = getExtras(i).getString(META_DATA_PARENT_DIRECTORY_PATH);
                    folderName = getExtras(i).getString(META_DATA_PARENT_DIRECTORY_NAME);
                }

                String key = folderPath;
                if (null == key) {
                    break;
                }
                if (!collection.containsKey(key)) {
                    MediaItem newFolder = createCollectionRootMediaItem(folderPath, folderName, folderPath);
                    getKeys().add(newFolder);
                    collection.put(key, new TreeSet<>(compareMediaItemsByTitle));
                }
                collection.get(key).add(i);
            }
        }
    }

    @Override
    public List<MediaItem> search(String query) {
        return null;
    }

//    @Override
//    public TreeSet<MediaItem> getChildren(LibraryObject libraryObject) {
//        if (getRootIdAsString().equals(libraryObject)) {
//            return getKeys();
//        }
//        for (MediaItem i : getKeys()) {
//            String mediaId = getMediaId(i);
//            if (mediaId != null && mediaId.equals(libraryObject.getId())) {
//                return collection.get(getMediaId(i));
//            }
//        }
//        return null;
//    }

    @Override
    public TreeSet<MediaItem> getChildren(LibraryObject id) {
        final String[] PROJECTION = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID};
        String selection = MediaStore.Audio.Media.DATA + " LIKE ?";
        String[] selectionArgs = {id.getId() + "%"};
        List<MediaBrowserCompat.MediaItem> listToReturn = new ArrayList<>();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,PROJECTION,
                selection, selectionArgs, null);

        while (cursor.moveToNext()) {
            listToReturn.add(createPlayableMediaItemFromCursor(cursor));
        }

        //contentResolver.query()
        return convertMediaItems(listToReturn, compareMediaItemsByTitle);
    }

    @Override
    public TreeSet<MediaItem> getAllChildren() {
        ContentResolver contentResolver = context.getContentResolver();
        TreeSet<MediaBrowserCompat.MediaItem> listToReturn = new TreeSet<>(ComparatorUtils.compareMediaItemsByTitle);
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,PROJECTION,
                null, null, null);
        while (cursor.moveToNext()) {
            listToReturn.add(createFolderMediaItem(cursor));
        }
        return listToReturn;
    }

    @Override
    public Category getRootId() {
        return Category.FOLDERS;
    }

    @Override
    public MediaItem build(Folder root) {
        return null;
    }

    @Override
    public TreeSet<MediaItem> convert(List<Folder> list) {
        return null;
    }



    private MediaBrowserCompat.MediaItem createFolderMediaItem(Cursor c){
        String path = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA));
        File file =  new File(path);
        File directory = file.getParentFile();

        String directoryName = null;
        String  directoryPath = null;

        if (null != directory) {
            directoryName = directory.getName();
            directoryPath = directory.getAbsolutePath();
        }


        Bundle extras = new Bundle();
        extras.putString(META_DATA_PARENT_DIRECTORY_NAME, directoryName);
        extras.putString(META_DATA_PARENT_DIRECTORY_PATH, directoryPath);


        // TODO: add code to fetch album art also
        MediaDescriptionCompat.Builder mediaDescriptionCompatBuilder = new MediaDescriptionCompat.Builder()
                .setMediaId(directoryPath)
                .setTitle(directoryName)
                .setDescription(directoryPath)
                .setExtras(extras);

        return new MediaBrowserCompat.MediaItem(mediaDescriptionCompatBuilder.build(), MediaItem.FLAG_BROWSABLE);
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
