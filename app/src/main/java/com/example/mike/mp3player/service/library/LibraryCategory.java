package com.example.mike.mp3player.service.library;

import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;

import java.util.List;

public abstract class LibraryCategory {

    private MediaBrowserCompat.MediaItem root;
    public abstract void index(List<MediaBrowserCompat.MediaItem> items);

    public LibraryCategory(String id, String title, String description) {
        this.root = createCollectionRootMediaItem(id, title, description);
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
}
