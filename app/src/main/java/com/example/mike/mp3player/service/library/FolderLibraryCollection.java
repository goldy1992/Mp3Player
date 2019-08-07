package com.example.mike.mp3player.service.library;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.ComparatorUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;

import java.io.File;
import java.util.List;
import java.util.TreeSet;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

public class FolderLibraryCollection extends LibraryCollection {

    private static final String[] PROJECTION = {
            "DISTINCT " + MediaStore.Audio.Media.DATA };

    private final String LOG_TAG = "FOLDER_LIB_COLLECTION";

    public FolderLibraryCollection(ContentResolver contentResolver) {
        super(contentResolver);
    }


    @Override
    public List<MediaItem> search(String query) {
        return null;
    }

    @Override
    public String getProjection() {
        return null;
    }

    @Override
    public TreeSet<MediaItem> getChildren(LibraryObject id) {
        String selection = MediaStore.Audio.Media.DATA + " LIKE ?";
        String[] selectionArgs = {id.getId() + "%"};
        return querySongs(selection, selectionArgs);
    }

    @Override
    public TreeSet<MediaItem> getAllChildren() {
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



}
