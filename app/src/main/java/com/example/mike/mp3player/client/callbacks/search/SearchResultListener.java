package com.example.mike.mp3player.client.callbacks.search;

import java.util.List;

import static android.support.v4.media.MediaBrowserCompat.MediaItem;

public interface SearchResultListener {

    void onSearchResult(List<MediaItem> searchResults);
}
