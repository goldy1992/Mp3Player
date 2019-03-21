package com.example.mike.mp3player.service.library;

import android.util.Range;

import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryId;

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
    public TreeSet<MediaItem> getChildren(LibraryId id, Range<Integer> range) {
        return null;
    }
    @Override
    public void index(List<MediaItem> items) {
        this.getKeys().addAll(items);
    }

    @Override
    public Category getRootId() {
        return Category.ROOT;
    }
}
