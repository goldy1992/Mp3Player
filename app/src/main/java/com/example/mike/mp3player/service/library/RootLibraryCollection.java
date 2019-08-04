package com.example.mike.mp3player.service.library;

import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;

import java.util.List;
import java.util.TreeSet;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByCategory;

public class RootLibraryCollection extends LibraryCollection {

    public static final String ID = Constants.CATEGORY_ROOT_ID;

    public RootLibraryCollection() {
        super(ID, ID, ID, compareRootMediaItemsByCategory, null);
    }

    @Override
    public TreeSet<MediaItem> getChildren(LibraryObject id) {
        return null;
    }
    @Override
    public void index(List<MediaItem> items) {
        this.getKeys().addAll(items);
    }

    @Override
    public List<MediaItem> search(String query) {
        return null;
    }

    @Override
    public Category getRootId() {
        return Category.ROOT;
    }
}
