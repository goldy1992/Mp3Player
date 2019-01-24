package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import java.util.ArrayList;
import java.util.List;

public abstract class LibraryCategory {

    private String rootId;
    private MediaBrowserCompat.MediaItem root;

    protected List<MediaBrowserCompat.MediaItem> keys;
    public abstract void index(List<MediaBrowserCompat.MediaItem> items);

    public LibraryCategory(String id, String title, String description) {
        this.rootId = id;
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

    public String getRootId() {
        return rootId;
    }

    public MediaBrowserCompat.MediaItem getRoot() {
        return root;
    }

    public List<MediaBrowserCompat.MediaItem> getKeys() {
        return keys;
    }
}
