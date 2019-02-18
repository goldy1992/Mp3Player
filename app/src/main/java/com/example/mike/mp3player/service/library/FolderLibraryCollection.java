package com.example.mike.mp3player.service.library;

import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryId;

import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.ComparatorUtils.compareMediaItemsByTitle;
import static com.example.mike.mp3player.commons.MediaItemUtils.getExtras;
import static com.example.mike.mp3player.commons.MediaItemUtils.getMediaId;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_NAME;
import static com.example.mike.mp3player.commons.MetaDataKeys.META_DATA_PARENT_DIRECTORY_PATH;

public class FolderLibraryCollection extends LibraryCollection {
    private final String LOG_TAG = "FOLDER_LIB_COLLECTION";
    public static final String ID = Constants.CATEGORY_FOLDERS_ID;
    public static final String TITLE = Constants.CATEGORY_FOLDERS_TITLE;
    public static final String DESCRIPTION = Constants.CATEGORY_FOLDERS_DESCRIPTION;

    public FolderLibraryCollection() {
        super(ID, TITLE, DESCRIPTION, compareMediaItemsByTitle, compareMediaItemsByTitle);
        this.collection = new TreeMap<>();
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
                    collection.put(key, new TreeSet<>(compareMediaItemsByTitle));
                }
                collection.get(key).add(i);
            }
        }
    }

    @Override
    public TreeSet<MediaItem> getChildren(LibraryId libraryId) {
        if (getRootIdAsString().equals(libraryId)) {
            return getKeys();
        }
        for (MediaItem i : getKeys()) {
            String mediaId = getMediaId(i);
            if (mediaId != null && mediaId.equals(libraryId.getId())) {
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
