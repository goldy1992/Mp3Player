package com.example.mike.mp3player.service.library.contentretriever;

import android.database.Cursor;
import android.support.v4.media.MediaBrowserCompat;

import androidx.annotation.NonNull;

import com.example.mike.mp3player.commons.MediaItemType;

import java.util.List;

public interface SearchableRetriever {
    List<MediaBrowserCompat.MediaItem> search(@NonNull String query);
    Cursor performSearchQuery(String query);
    MediaItemType getSearchCategory();
}
