package com.github.goldy1992.mp3player.service.library.content.filter;

import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import java.util.List;

public interface ResultsFilter {

    List<MediaItem> filter(@NonNull String query, @NonNull List<MediaItem> results);
}
