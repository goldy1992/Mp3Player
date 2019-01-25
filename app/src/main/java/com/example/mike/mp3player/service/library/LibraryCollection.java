package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.library.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class LibraryCollection {

    private final Category id;
    private final MediaBrowserCompat.MediaItem root;
    private List<MediaBrowserCompat.MediaItem> keys;
    protected Map<String, List<MediaBrowserCompat.MediaItem>> collection;
    public abstract List<MediaBrowserCompat.MediaItem> getChildren(String id);
    public abstract void index(List<MediaBrowserCompat.MediaItem> items);


    public LibraryCollection(Category categoryId, String id, String title, String description) {
        this.id = categoryId;
        this.root = createCollectionRootMediaItem(id, title, description);
        this.keys = new ArrayList<>();
    }

    MediaBrowserCompat.MediaItem createCollectionRootMediaItem(String id, String title, String description) {
        MediaDescriptionCompat foldersDescription = new MediaDescriptionCompat.Builder()
                .setDescription(description)
                .setTitle(title)
                .setMediaId(id)
                .build();
        return new MediaBrowserCompat.MediaItem(foldersDescription, MediaBrowserCompat.MediaItem.FLAG_BROWSABLE);
    }

    public Category getRootId() {
        return Category.ROOT;
    }

    public MediaBrowserCompat.MediaItem getRoot() {
        return root;
    }

    public List<MediaBrowserCompat.MediaItem> getKeys() {
        return keys;
    }
}
