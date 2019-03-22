package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Pair;

import com.example.mike.mp3player.commons.MediaItemUtils;
import com.example.mike.mp3player.commons.Range;
import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryId;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static com.example.mike.mp3player.commons.MediaItemUtils.getRangeBoundItem;

public abstract class LibraryCollection {

    private Comparator<MediaItem> keyComparator;
    private Comparator<MediaItem> valueComparator;
    private final MediaItem root;
    private TreeSet<MediaItem> keys;
    protected Map<String, TreeSet<MediaItem>> collection;

    /**
     * Returns a subset of MediaItems within a given range
     * @param id the library item of which to get the children
     * @param range the range on indexes to select from
     * @return a subset of the library items children or null if invalid range
     */
    public Set<MediaItem> getChildren(LibraryId id, Range range) {
        if (getRootIdAsString().equals(id.getId())) {
            return getKeys(range);
        }
        TreeSet<MediaItem> children = collection.get(id.getId());
        if (range == null) {
            return children;
        }
        Pair<MediaItem, MediaItem> subsetBounds = MediaItemUtils.getRangeBoundItem(range, children);
        return children.subSet(subsetBounds.first, true, subsetBounds.second, true);
    }

    public abstract void index(List<MediaItem> items);
    public abstract Category getRootId();

    @SuppressWarnings("unchecked")
    public LibraryCollection(String id, String title, String description, Comparator keyComparator, Comparator valueComparator) {
        this.root = createCollectionRootMediaItem(id, title, description);
        this.keyComparator = keyComparator;
        this.valueComparator = valueComparator;
        this.keys = new TreeSet<>(this.getKeyComparator());
    }

    MediaItem createCollectionRootMediaItem(String id, String title, String description) {
        MediaDescriptionCompat foldersDescription = new MediaDescriptionCompat.Builder()
                .setDescription(description)
                .setTitle(title)
                .setMediaId(id)
                .build();
        return new MediaItem(foldersDescription, MediaItem.FLAG_BROWSABLE);
    }

    public MediaItem getRoot() {
        return root;
    }

    public String getRootIdAsString() {
        return getRootId().name();
    }

    public Set<MediaItem> getKeys() {
        return keys;
    }

    public Set<MediaItem> getKeys(Range range) {
        if (range == null) {
            return keys;
        }
        Pair<MediaItem, MediaItem> subsetBounds = MediaItemUtils.getRangeBoundItem(range, keys);
        return keys.subSet(subsetBounds.first, true, subsetBounds.second, true);
    }

    public Comparator<MediaItem> getKeyComparator() {
        return keyComparator;
    }

    public Comparator<MediaItem> getValueComparator() {
        return valueComparator;
    }

}
