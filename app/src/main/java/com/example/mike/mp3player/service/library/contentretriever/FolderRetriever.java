package com.example.mike.mp3player.service.library.contentretriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE_ID;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

public class FolderRetriever extends ContentResolverRetriever {

    private static final String[] PROJECTION = {
            "DISTINCT " + MediaStore.Audio.Media.DATA };

    public FolderRetriever(ContentResolver contentResolver, String typeId) {
        super(contentResolver, typeId);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.FOLDER;
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

        return createResultList(cursor);

    }

    @Override
    public List<MediaBrowserCompat.MediaItem> search(@NonNull String query) {
        return null;
    }

    private List<MediaBrowserCompat.MediaItem> createResultList(Cursor cursor) {
        List<MediaBrowserCompat.MediaItem> listToReturn = new ArrayList<>();
        Set<String> directoryPathSet = new HashSet<>();

        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            File file = new File(path);
            File directory = file.getParentFile();

            String directoryName = null;
            String directoryPath = null;

            if (null != directory) {
                directoryName = directory.getName();
                directoryPath = directory.getAbsolutePath();

                if (directoryPathSet.add(directoryPath)) {
                    listToReturn.add(createFolderMediaItem(directoryName, directoryPath));
                }
            }

        }
        return listToReturn;
    }

    private MediaBrowserCompat.MediaItem createFolderMediaItem(String directoryName, String directoryPath){


        Bundle extras = new Bundle();
        extras.putString(META_DATA_PARENT_DIRECTORY_NAME, directoryName);
        extras.putString(META_DATA_PARENT_DIRECTORY_PATH, directoryPath);
        extras.putSerializable(MEDIA_ITEM_TYPE, getType());
        extras.putString(MEDIA_ITEM_TYPE_ID, childId);


        // TODO: add code to fetch album art also
        MediaDescriptionCompat.Builder mediaDescriptionCompatBuilder = new MediaDescriptionCompat.Builder()
                .setMediaId(directoryPath)
                .setTitle(directoryName)
                .setDescription(directoryPath)
                .setExtras(extras);

        return new MediaBrowserCompat.MediaItem(mediaDescriptionCompatBuilder.build(), MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);
    }
}
