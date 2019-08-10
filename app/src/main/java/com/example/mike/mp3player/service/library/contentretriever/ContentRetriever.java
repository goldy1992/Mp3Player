package com.example.mike.mp3player.service.library.contentretriever;

import android.support.v4.media.MediaBrowserCompat;


import androidx.annotation.NonNull;

import java.util.List;

public interface ContentRetriever {
    List<MediaBrowserCompat.MediaItem> getChildren(String id);
    List<MediaBrowserCompat.MediaItem> search(@NonNull String query);
}
