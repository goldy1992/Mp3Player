package com.github.goldy1992.mp3player.service.library.content.parser;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import com.github.goldy1992.mp3player.commons.ComparatorUtils;
import com.github.goldy1992.mp3player.commons.MediaItemBuilder;
import com.github.goldy1992.mp3player.commons.MediaItemType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import static android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_BROWSABLE;
import static com.github.goldy1992.mp3player.commons.Constants.ID_SEPARATOR;
import static com.github.goldy1992.mp3player.commons.MediaItemUtils.getDirectoryPath;

public class FolderResultsParser extends ResultsParser {


    @Inject
    public FolderResultsParser() {
        super();
    }

    @Override
    public List<MediaItem> create(@NonNull Cursor cursor, String mediaIdPrefix) {
        TreeSet<MediaItem> listToReturn = new TreeSet<>(this);
        Set<String> directoryPathSet = new HashSet<>();
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            File file = new File(path);
            if (file.exists()) {
                File directory = file.getParentFile();
                String directoryPath;

                if (null != directory) {
                    directoryPath = directory.getAbsolutePath();
                    if (directoryPathSet.add(directoryPath)) {
                        MediaItem mediaItem = createFolderMediaItem(directory, mediaIdPrefix);
                        listToReturn.add(mediaItem);
                    }
                }
            }
        }
        return new ArrayList<>(listToReturn);
    }

    @Override
    public MediaItemType getType() {
        return MediaItemType.FOLDER;
    }


    private MediaItem createFolderMediaItem(File folder, String parentId) {
        /* append a file separator so that folders with an "extended" name are discarded...
         * e.g. Folder to accept: 'folder1'
         *      Folder to reject: 'folder1extended' */
        String filePath = folder.getAbsolutePath() + File.separator;
        return new MediaItemBuilder(filePath)
        .setMediaItemType(MediaItemType.FOLDER)
        .setLibraryId(buildLibraryId(parentId, filePath))
        .setDirectoryFile(folder)
        .setFlags(FLAG_BROWSABLE)
        .build();
    }

    @Override
    public int compare(MediaItem m1, MediaItem m2) {
        return ComparatorUtils.Companion.caseSensitiveStringCompare.compare(getDirectoryPath(m1), getDirectoryPath(m2));
    }

    private String buildLibraryId(String prefix, String childItemId) {
        return new StringBuilder()
                .append(prefix)
                .append(ID_SEPARATOR)
                .append(childItemId)
                .toString();
    }
}
