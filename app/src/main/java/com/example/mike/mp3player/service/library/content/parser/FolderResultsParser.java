package com.example.mike.mp3player.service.library.content.parser;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.MediaItemType;
import com.example.mike.mp3player.service.library.utils.IdGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static com.example.mike.mp3player.commons.ComparatorUtils.uppercaseStringCompare;
import static com.example.mike.mp3player.commons.Constants.LIBRARY_ID;
import static com.example.mike.mp3player.commons.MediaItemUtils.getTitle;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

public class FolderResultsParser extends ResultsParser {

    @Override
    public List<MediaBrowserCompat.MediaItem> create(Cursor cursor, String mediaIdPrefix) {
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
                    MediaBrowserCompat.MediaItem mediaItem = createFolderMediaItem(directoryName, directoryPath, mediaIdPrefix);
                    listToReturn.add(mediaItem);
                }
            }
        }
        return new ArrayList<>(listToReturn);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.FOLDER;
    }


    private MediaBrowserCompat.MediaItem createFolderMediaItem(String directoryName, String directoryPath, String parentId){
        Bundle extras = getExtras();
        extras.putString(LIBRARY_ID, buildLibraryId(parentId, directoryPath));
        MediaDescriptionCompat.Builder mediaDescriptionCompatBuilder = new MediaDescriptionCompat.Builder()
                .setMediaId(directoryPath)
                .setTitle(directoryName)
                .setDescription(directoryPath)
                .setExtras(extras);

        return new MediaBrowserCompat.MediaItem(mediaDescriptionCompatBuilder.build(), MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);
    }

    @Override
    public int compare(MediaBrowserCompat.MediaItem m1, MediaBrowserCompat.MediaItem m2) {
        return uppercaseStringCompare(getTitle(m1), getTitle(m2));
    }

    private String buildLibraryId(String prefix, String childItemId) {
        return new StringBuilder()
                .append(prefix)
                .append(IdGenerator.ID_SEPARATOR)
                .append(childItemId)
                .toString();
    }
}
