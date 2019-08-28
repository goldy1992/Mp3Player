package com.example.mike.mp3player.service.library.contentretriever;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.ContentRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.example.mike.mp3player.commons.ComparatorUtils.uppercaseStringCompare;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

public class FoldersRetriever extends ContentResolverRetriever implements SearchableRetriever {

    private static final String[] PROJECTION = {
            "DISTINCT " + MediaStore.Audio.Media.DATA };

    public FoldersRetriever(ContentResolver contentResolver, String idPrefix) {
        super(contentResolver, idPrefix);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.FOLDER;
    }

    @Override
    public MediaItemType getSearchCategory() {
        return MediaItemType.FOLDERS;
    }

    @Override
    Cursor performGetChildrenQuery(String id) {
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,PROJECTION,
                null, null, null);
    }

    @Override
    public Cursor performSearchQuery(String query) {
        final String WHERE = MediaStore.Audio.Media.DATA + " LIKE ? COLLATE NOCASE";
        final String[] WHERE_ARGS = { "%" + query + "%"};
        return contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI ,PROJECTION,
                WHERE, WHERE_ARGS, null);
    }

    @Override
    public String[] getProjection() {
        return PROJECTION;
    }

    @Override
    MediaBrowserCompat.MediaItem buildMediaItem(Cursor cursor, String parentId) {
        return null;
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> getChildren(ContentRequest request) {
        Cursor cursor = performGetChildrenQuery(request.getSearchString());
        return accumulateResults(cursor);
    }

    @Override
    public List<MediaBrowserCompat.MediaItem> search(@NonNull String query) {
        Cursor cursor = performSearchQuery(query);
        return accumulateSearchResults(cursor, query);
    }

    private List<MediaBrowserCompat.MediaItem> accumulateResults(Cursor cursor)
    {
        TreeSet<MediaBrowserCompat.MediaItem> listToReturn = new TreeSet<>(this);
        Set<String> directoryPathSet = new HashSet<>();
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            File file = new File(path);
            File directory = file.getParentFile();
            String directoryName;
            String directoryPath;

            if (null != directory) {
                directoryName = directory.getName();
                directoryPath = directory.getAbsolutePath();
                if (directoryPathSet.add(directoryPath)) {
                    MediaBrowserCompat.MediaItem mediaItem = createFolderMediaItem(directoryName, directoryPath, null);
                    listToReturn.add(mediaItem);
                }
            }
        }
        return new ArrayList<>(listToReturn);
    }

    private List<MediaBrowserCompat.MediaItem> accumulateSearchResults(Cursor cursor, String query) {
        TreeSet<MediaBrowserCompat.MediaItem> listToReturn = new TreeSet<>(this);
        Set<String> directoryPathSet = new HashSet<>();
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            File file = new File(path);
            File directory = file.getParentFile();
            String directoryName;
            String directoryPath;

            if (null != directory) {
                directoryName = directory.getName();
                if (null != directoryName && directoryName.toUpperCase().contains(query.toUpperCase())) {
                    directoryPath = directory.getAbsolutePath();
                    if (directoryPathSet.add(directoryPath)) {
                        MediaBrowserCompat.MediaItem mediaItem = createFolderMediaItem(directoryName, directoryPath, null);
                        listToReturn.add(mediaItem);
                    }
                }
            }

        }
        return new ArrayList<>(listToReturn);
    }
    private MediaBrowserCompat.MediaItem createFolderMediaItem(String directoryName, String directoryPath, String parentId){
        Bundle extras = getExtras();
        extras.putString(META_DATA_PARENT_DIRECTORY_NAME, directoryName);
        extras.putString(META_DATA_PARENT_DIRECTORY_PATH, directoryPath);

        MediaDescriptionCompat.Builder mediaDescriptionCompatBuilder = new MediaDescriptionCompat.Builder()
                .setMediaId(buildMediaId(parentId, directoryPath))
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
