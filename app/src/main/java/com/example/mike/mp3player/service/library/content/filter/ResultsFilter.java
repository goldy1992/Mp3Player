package com.example.mike.mp3player.service.library.content.filter;

import android.support.v4.media.MediaBrowserCompat.MediaItem;

import androidx.annotation.NonNull;

import java.util.List;

public abstract class ResultsFilter {

    public abstract List<MediaItem> filter(@NonNull String query, @NonNull List<MediaItem> results);
}