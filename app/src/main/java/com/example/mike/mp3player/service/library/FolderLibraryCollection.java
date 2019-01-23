package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;
import android.util.Log;

import com.example.mike.mp3player.commons.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

public class FolderLibraryCollection extends LibraryCollection {
    private final String LOG_TAG = "FOLDER_LIB_COLLECTION";
    public static final String ID = Constants.CATEGORY_FOLDERS_ID;
    public static final String TITLE = Constants.CATEGORY_FOLDERS_TITLE;
    public static final String DESCRIPTION = Constants.CATEGORY_FOLDERS_DESCRIPTION;

    public FolderLibraryCollection() {
        super(ID, TITLE, DESCRIPTION);
        this.collection = new HashMap<MediaBrowserCompat.MediaItem, List<MediaBrowserCompat.MediaItem>>();
    }

    @Override
    public void index(List<MediaBrowserCompat.MediaItem> items) {

        for (MediaBrowserCompat.MediaItem i : items) {
            String parentDirectoryPath = i.getDescription().getExtras().getString(META_DATA_PARENT_DIRECTORY_PATH);
            String parentDirectory = i.getDescription().getExtras().getString(META_DATA_PARENT_DIRECTORY_NAME);

            MediaBrowserCompat.MediaItem key = getKeyFromParentPath(parentDirectoryPath);
            if (null == key) {
                key = createCollectionRootMediaItem(parentDirectoryPath, parentDirectory, parentDirectory);
                collection.put(key, new ArrayList<>());
            }
            collection.get(key).add(i);

        }
    }

    private boolean parentPathInCollection(String parentDirectoryPath) {
            for (MediaBrowserCompat.MediaItem i : collection.keySet()) {
                Log.d(LOG_TAG, i.getDescription().getTitle() + "");
                String itemPath = i.getDescription().getMediaId();
                if (itemPath != null && itemPath.equals(parentDirectoryPath)) {
                    return true;
                }
            }
            return false;
    }

    private MediaBrowserCompat.MediaItem getKeyFromParentPath(String parentDirectoryPath) {
        for (MediaBrowserCompat.MediaItem i : collection.keySet()) {

            String itemPath = i.getDescription().getMediaId();
            if (itemPath != null && itemPath.equals(parentDirectoryPath)) {
                return i;
            }
        }
        return null;
    }


}
