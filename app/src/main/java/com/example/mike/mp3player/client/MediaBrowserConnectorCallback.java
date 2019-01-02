package com.example.mike.mp3player.client;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;

import java.util.List;

import androidx.annotation.NonNull;

public interface MediaBrowserConnectorCallback {
    void onChildrenLoaded(@NonNull String parentId,
                          @NonNull List<MediaBrowserCompat.MediaItem> children,
                          @NonNull Bundle options);
}
