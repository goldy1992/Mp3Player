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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.mike.mp3player.commons.ComparatorUtils.uppercaseStringCompare;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE;
import static com.example.mike.mp3player.commons.Constants.MEDIA_ITEM_TYPE_ID;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

public class FoldersRetriever extends ContentResolverRetriever {

    private static final String[] PROJECTION = {
            "DISTINCT " + MediaStore.Audio.Media.DATA };

    Set<String> directoryPathSet = new HashSet<>();

    public FoldersRetriever(ContentResolver contentResolver, String idPrefix) {
        super(contentResolver, idPrefix);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.FOLDER;
    }

    @Override
    public MediaItemType getParentType() {
        return MediaItemType.FOLDERS;
    }

    @Override
    Cursor getResults(String id) {
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,PROJECTION,
                null, null, null);
    }

    @Override
    public String[] getProjection() {
        return PROJECTION;
    }

    @Override
    MediaBrowserCompat.MediaItem buildMediaItem(Cursor cursor) {
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        File file = new File(path);
        File directory = file.getParentFile();

        String directoryName = null;
        String directoryPath = null;

        if (null != directory) {
            directoryName = directory.getName();
            directoryPath = directory.getAbsolutePath();

            if (directoryPathSet.add(directoryPath)) {
                return createFolderMediaItem(directoryName, directoryPath);
            }
        }
        return null;
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> search(@NonNull String query) {
        return null;
    }

    private MediaBrowserCompat.MediaItem createFolderMediaItem(String directoryName, String directoryPath){
        Bundle extras = new Bundle();
        extras.putString(META_DATA_PARENT_DIRECTORY_NAME, directoryName);
        extras.putString(META_DATA_PARENT_DIRECTORY_PATH, directoryPath);

        MediaDescriptionCompat.Builder mediaDescriptionCompatBuilder = new MediaDescriptionCompat.Builder()
                .setMediaId(idPrefix + directoryPath)
                .setTitle(directoryName)
                .setDescription(directoryPath)
                .setExtras(extras);

        return new MediaBrowserCompat.MediaItem(mediaDescriptionCompatBuilder.build(), MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);
    }

    @Override
    public int compare(MediaBrowserCompat.MediaItem m1, MediaBrowserCompat.MediaItem m2) {
        return uppercaseStringCompare(getTitle(m1), getTitle(m2));
    }
}
