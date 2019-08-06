package com.example.mike.mp3player.service.library;

import android.content.ContentResolver;
import android.content.Context;
import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.example.mike.mp3player.commons.ComparatorUtils;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryObject;
import com.example.mike.mp3player.service.library.db.CategoryDao;
import com.example.mike.mp3player.service.library.db.CategoryEntity;
import com.example.mike.mp3player.service.library.db.Root;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

@Dao
public abstract class LibraryCollection {

    protected final Context context;
    protected final CategoryDao dao;
    private Comparator<MediaItem> keyComparator;
    private Comparator<MediaItem> valueComparator;
    private MediaItem root;
    private TreeSet<MediaItem> keys;
    protected Map<String, TreeSet<MediaItem>> collection;
    public abstract TreeSet<MediaItem> getChildren(LibraryObject id);
    public abstract TreeSet<MediaItem> getAllChildren();
    public abstract void index(List<MediaItem> items);
    public abstract List<MediaItem> search(String query);
    public abstract Category getRootId();
//    public abstract MediaItem build(C root);
//    public abstract TreeSet<MediaItem> convert(List<C> list);
    protected ContentResolver contentResolver;

    @SuppressWarnings("unchecked")
    public LibraryCollection(String id, String title, String description, Comparator keyComparator, Comparator valueComparator, final CategoryDao dao, Context context) {
        this.root = createCollectionRootMediaItem(id, title, description);
        this.keyComparator = keyComparator;
        this.valueComparator = valueComparator;
        this.keys = new TreeSet<>(this.getKeyComparator());
        this.dao = dao;
        this.contentResolver = context.getContentResolver();
        this.context = context;
    }

    MediaItem createCollectionRootMediaItem(String id, String title, String description) {
        MediaDescriptionCompat foldersDescription = new MediaDescriptionCompat.Builder()
                .setDescription(description)
                .setTitle(title)
                .setMediaId(id)
                .build();
        return new MediaItem(foldersDescription, MediaItem.FLAG_BROWSABLE);
    }


//    public TreeSet<MediaItem> convert(List<C> roots, Comparator<MediaItem> comparator) {
//        TreeSet<MediaItem> mediaItemTreeSet = new TreeSet<>(comparator);
//        for (C root : roots) {
//            mediaItemTreeSet.add(build(root));
//        }
//        return mediaItemTreeSet;
//    }

    public TreeSet<MediaItem> convertMediaItems(List<MediaItem> roots, Comparator<MediaItem> comparator) {
        TreeSet<MediaItem> mediaItemTreeSet = new TreeSet<>(comparator);
        for (MediaItem root : roots) {
            mediaItemTreeSet.add(root);
        }
        return mediaItemTreeSet;
    }

    public MediaItem getRoot() {
        return root;
    }

    public String getRootIdAsString() {
        return getRootId().name();
    }

    public TreeSet<MediaItem> getKeys() {
        return keys;
    }

    public Comparator<MediaItem> getKeyComparator() {
        return keyComparator;
    }

    public Comparator<MediaItem> getValueComparator() {
        return valueComparator;
    }
}
