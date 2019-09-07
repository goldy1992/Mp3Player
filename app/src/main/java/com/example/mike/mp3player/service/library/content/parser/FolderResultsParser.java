package com.example.mike.mp3player.service.library.content.parser;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat;

import com.example.mike.mp3player.commons.MediaItemBuilder;
import com.example.mike.mp3player.commons.MediaItemType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE;
import static com.example.mike.mp3player.commons.ComparatorUtils.caseSensitiveStringCompare;
import static com.example.mike.mp3player.commons.Constants.ID_SEPARATOR;
import static com.example.mike.mp3player.commons.MediaItemUtils.getDirectoryPath;

public class FolderResultsParser extends ResultsParser {

    @Override
    public List<MediaBrowserCompat.MediaItem> create(Cursor cursor, String mediaIdPrefix) {
        TreeSet<MediaBrowserCompat.MediaItem> listToReturn = new TreeSet<>(this);
        Set<String> directoryPathSet = new HashSet<>();
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            File file = new File(path);
            File directory = file.getParentFile();
            String directoryPath;

            if (null != directory) {
                directoryPath = directory.getAbsolutePath();
                if (directoryPathSet.add(directoryPath)) {
                    MediaBrowserCompat.MediaItem mediaItem = createFolderMediaItem(directory, mediaIdPrefix);
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


    private MediaBrowserCompat.MediaItem createFolderMediaItem(File file, String parentId) {
        String filePath = file.getAbsolutePath();
        return new MediaItemBuilder(filePath)
        .setMediaItemType(MediaItemType.FOLDER)
        .setLibraryId(buildLibraryId(parentId, filePath))
        .setFile(file)
        .setFlags(FLAG_BROWSABLE)
        .build();
    }

    @Override
    public int compare(MediaBrowserCompat.MediaItem m1, MediaBrowserCompat.MediaItem m2) {
        return caseSensitiveStringCompare(getDirectoryPath(m1), getDirectoryPath(m2));
    }

    private String buildLibraryId(String prefix, String childItemId) {
        return new StringBuilder()
                .append(prefix)
                .append(ID_SEPARATOR)
                .append(childItemId)
                .toString();
    }
}
