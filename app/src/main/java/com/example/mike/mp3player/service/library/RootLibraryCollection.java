package com.example.mike.mp3player.service.library;

import android.content.Context;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.ComparatorUtils;
import com.example.mike.mp3player.commons.Constants;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.service.library.db.CategoryDao;
import com.example.mike.mp3player.service.library.db.Root;
import com.example.mike.mp3player.service.library.db.RootDao;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;
import static com.example.mike.mp3player.commons.ComparatorUtils.compareRootMediaItemsByCategory;

public class RootLibraryCollection extends LibraryCollection<Root> {

    public static final String ID = Constants.CATEGORY_ROOT_ID;

    public RootLibraryCollection(final RootDao dao, Context context) {
        super(ID, ID, ID, compareRootMediaItemsByCategory, null, dao, context);
        List<Root> rootList = new ArrayList<>();
        for (Category category : Category.values()) {
            Root root = new Root();
            root.category = category;
            rootList.add(root);
        }
        dao.insertAll(rootList.toArray(new Root[rootList.size()]));
    }

    @Override
    public TreeSet<MediaItem> getChildren(LibraryObject id) {
        return null;
    }

    @Override
    public TreeSet<MediaItem> getAllChildren() {
        List<Root> roots = ((RootDao)dao).getAll();
        return convert(roots);
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

    @Override
    public MediaItem build(Root root) {
        String name = root.category.name();
        MediaDescriptionCompat description = new MediaDescriptionCompat.Builder()
                .setMediaId(name)
                .setTitle(name)
                .setDescription(name)
                .build();
        return new MediaItem(description, MediaItem.FLAG_BROWSABLE);
    }

    @Override
    public TreeSet<MediaItem> convert(List<Root> roots) {
        TreeSet<MediaItem> mediaItemTreeSet = new TreeSet<>(ComparatorUtils.compareRootMediaItemsByCategory);
        for (Root root : roots) {
            mediaItemTreeSet.add(build(root));
        }
        return mediaItemTreeSet;
    }
}
