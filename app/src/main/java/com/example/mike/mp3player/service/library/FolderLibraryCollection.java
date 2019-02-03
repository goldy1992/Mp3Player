package com.example.mike.mp3player.service.library;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.MediaItemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.support.v4.media.MediaBrowserCompat.*;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;
import static com.example.mike.mp3player.commons.MediaItemUtils.getExtras;
import static com.example.mike.mp3player.commons.MediaItemUtils.getMediaId;

public class FolderLibraryCollection extends LibraryCollection {
    private final String LOG_TAG = "FOLDER_LIB_COLLECTION";
    public static final String ID = Constants.CATEGORY_FOLDERS_ID;
    public static final String TITLE = Constants.CATEGORY_FOLDERS_TITLE;
    public static final String DESCRIPTION = Constants.CATEGORY_FOLDERS_DESCRIPTION;

    public FolderLibraryCollection() {
        super(ID, TITLE, DESCRIPTION);
        this.collection = new HashMap<>();
    }

    @Override
    public void index(List<MediaItem> items) {
        if (null != items) {
            for (MediaItem i : items) {
                String parentDirectoryPath = null;
                String parentDirectoryName = null;

                if (MediaItemUtils.hasExtras(i)) {
                    parentDirectoryPath = getExtras(i).getString(META_DATA_PARENT_DIRECTORY_PATH);
                    parentDirectoryName = getExtras(i).getString(META_DATA_PARENT_DIRECTORY_NAME);
                }

                String key = parentDirectoryPath;
                if (null == key) {
                    break;
                }
                if (!collection.containsKey(key)) {
                    getKeys().add(createCollectionRootMediaItem(parentDirectoryPath, parentDirectoryName, parentDirectoryPath));
                    collection.put(key, new ArrayList<>());
                }
                collection.get(key).add(i);
            }
        }
    }

    @Override
    public List<MediaItem> getChildren(String id) {
        if (getRootId().name().equals(id)) {
            return getKeys();
        }
        for (MediaItem i : getKeys()) {
            String mediaId = getMediaId(i);
            if (mediaId.equals(id)) {
                return collection.get(getMediaId(i));
            }
        }
        return null;
    }

    @Override
    public Category getRootId() {
        return Category.FOLDERS;
    }
}
