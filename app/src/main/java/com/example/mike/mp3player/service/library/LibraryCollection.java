package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat.MediaItem;
import android.support.v4.media.MediaDescriptionCompat;
import android.util.Range;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryId;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public abstract class LibraryCollection {

    private Comparator<MediaItem> keyComparator;
    private Comparator<MediaItem> valueComparator;
    private final MediaItem root;
    private TreeSet<MediaItem> keys;
    protected Map<String, TreeSet<MediaItem>> collection;

    /**
     * Returns a Set of
     * @param id
     * @param range
     * @return
     */
    public abstract Set<MediaItem> getChildren(LibraryId id, Range<Integer> range);
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
