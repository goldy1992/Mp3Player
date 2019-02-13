package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import com.example.mike.mp3player.commons.library.Category;
import com.example.mike.mp3player.commons.library.LibraryId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class LibraryCollection {

    private final MediaBrowserCompat.MediaItem root;
    private List<MediaBrowserCompat.MediaItem> keys;
    protected Map<String, List<MediaBrowserCompat.MediaItem>> collection;
    @Deprecated
    public abstract List<MediaBrowserCompat.MediaItem> getChildren(String id);

    public abstract List<MediaBrowserCompat.MediaItem> getChildren(LibraryId id);
    public abstract void index(List<MediaBrowserCompat.MediaItem> items);
    public abstract Category getRootId();

    public LibraryCollection(String id, String title, String description) {
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

    public MediaBrowserCompat.MediaItem getRoot() {
        return root;
    }

    public String getRootIdAsString() {
        return getRootId().name();
    }

    public List<MediaBrowserCompat.MediaItem> getKeys() {
        return keys;
    }
}
