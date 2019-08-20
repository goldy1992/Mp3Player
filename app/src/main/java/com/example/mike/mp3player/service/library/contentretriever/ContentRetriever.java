package com.example.mike.mp3player.service.library.contentretriever;

import android.support.v4.media.MediaBrowserCompat;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;
import java.util.List;

public interface ContentRetriever extends Comparator<MediaBrowserCompat.MediaItem> {
    List<MediaBrowserCompat.MediaItem> getChildren(@Nullable String id);
    List<MediaBrowserCompat.MediaItem> search(@NonNull String query);
}
